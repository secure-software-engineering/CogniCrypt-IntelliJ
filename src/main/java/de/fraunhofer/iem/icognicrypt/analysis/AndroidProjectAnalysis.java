package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.callgraph.ObservableDynamicICFG;
import boomerang.callgraph.ObservableICFG;
import boomerang.callgraph.ObservableStaticICFG;
import boomerang.preanalysis.BoomerangPretransformer;
import com.google.common.collect.Lists;
import crypto.analysis.CrySLResultsReporter;
import crypto.analysis.CryptoScanner;
import crypto.rules.CryptSLRule;
import crypto.rules.CryptSLRuleReader;
import de.fraunhofer.iem.icognicrypt.results.AnalysisListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.android.config.SootConfigForAndroid;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.options.Options;
import soot.util.queue.QueueReader;

import java.io.File;
import java.util.List;


public class AndroidProjectAnalysis extends JavaProjectAnalysis {

    private static final Logger logger = LoggerFactory.getLogger(AndroidProjectAnalysis.class);
    public AndroidProjectAnalysis(String apkFile, String pathToPlatforms, String rulesDir) {
        super(apkFile,pathToPlatforms,rulesDir);
    }

    @Override
    public void run() {
        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setAndroidPlatformDir(wholeClassPath);
        config.getAnalysisFileConfig().setTargetAPKFile(applicationClassPath);
        config.setMergeDexFiles(true);
        SetupApplication flowDroid = new SetupApplication(config);
        SootConfigForAndroid sootConfigForAndroid =
                new SootConfigForAndroid() {
                    @Override
                    public void setSootOptions(Options options, InfoflowConfiguration config) {
                        options.set_keep_line_number(true);
                    }
                };
        flowDroid.setSootConfig(sootConfigForAndroid);
        logger.info("Constructing call graph");
        flowDroid.constructCallgraph();
        logger.info("Done constructing call graph");

        runCryptoAnalysis();
    }

    private void runCryptoAnalysis() {
        BoomerangPretransformer.v().reset();
        BoomerangPretransformer.v().apply();

        ObservableStaticICFG icfg = new ObservableStaticICFG(new JimpleBasedInterproceduralCFG(false));

        final CrySLResultsReporter reporter = new CrySLResultsReporter();
        reporter.addReportListener(new AnalysisListener());
        CryptoScanner scanner = new CryptoScanner() {

            @Override
            public ObservableICFG<Unit, SootMethod> icfg() {
                return icfg;
            }

            @Override
            public CrySLResultsReporter getAnalysisListener() {
                return reporter;
            }

        };
        List<CryptSLRule> rules = getRules();
        logger.info("Loaded {} CrySL rules",rules.size());
        logger.info("Running CogniCrypt Analysis");
        scanner.scan(rules);
        logger.info("Terminated CogniCrypt Analysis");
    }


    protected List<CryptSLRule> getRules() {
        List<CryptSLRule> rules = Lists.newArrayList();
        if(rulesDirectory == null){
            throw new RuntimeException("Please specify a directory the CrySL rules (.cryptslbin Files) are located in.");
        }
        File[] listFiles = new File(rulesDirectory).listFiles();
        for (File file : listFiles) {
            if (file != null && file.getName().endsWith(".cryptslbin")) {
                rules.add(CryptSLRuleReader.readFromFile(file));
            }
        }
        if (rules.isEmpty())
            System.out.println(
                    "CogniCrypt did not find any rules to start the analysis for. \n It checked for rules in "
                            + rulesDirectory);
        return rules;
    }
}
