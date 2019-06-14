package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.callgraph.ObservableDynamicICFG;
import boomerang.callgraph.ObservableICFG;
import boomerang.callgraph.ObservableStaticICFG;
import boomerang.preanalysis.BoomerangPretransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.progress.ProgressIndicator;
import crypto.analysis.CrySLResultsReporter;
import crypto.analysis.CryptoScanner;
import crypto.rules.CryptSLRule;
import crypto.rules.CryptSLRuleReader;
import de.fraunhofer.iem.icognicrypt.results.AnalysisListener;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import soot.*;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.android.config.SootConfigForAndroid;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.options.Options;
import soot.util.queue.QueueReader;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class AndroidProjectAnalysis {

    private static final Logger logger = Logger.getInstance(AndroidProjectAnalysis.class);
    private final Set<String> javaSourceClassNames = Sets.newHashSet();
    protected final String apkFile;
    protected final String platformsDirectory;
    protected final String rulesDirectory;


    public AndroidProjectAnalysis(String apkFile, String platformsDirectory, String rulesDirectory, Collection<File> javaSourceFiles) {
        this.apkFile = apkFile;
        this.platformsDirectory = platformsDirectory;
        this.rulesDirectory = rulesDirectory;
        for(File f : javaSourceFiles){
            this.javaSourceClassNames.add(f.getName().replace(".java",""));
        }
    }

    public void run() {
        logger.info("Running static analysis on APK file " + apkFile);
        logger.info("with Android Platforms dir "+ platformsDirectory);
        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        config.getCallbackConfig().setEnableCallbacks(false);
        config.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        config.getAnalysisFileConfig().setAndroidPlatformDir(platformsDirectory);
        config.getAnalysisFileConfig().setTargetAPKFile(apkFile);
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
        prepareAnalysis();

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
        logger.info("Loaded "+ rules.size() + " CrySL rules");
        logger.info("Running CogniCrypt Analysis");
        scanner.scan(rules);
        logger.info("Terminated CogniCrypt Analysis");
        System.gc();
    }

    private void prepareAnalysis() {
        BoomerangPretransformer.v().reset();
        BoomerangPretransformer.v().apply();

        //Setting application classes to be the set of classes where we have found .java files for. Hereby we ignore library classes and reduce the analysis time.
        for(SootClass c : Scene.v().getClasses()){
            boolean applicationClass = false;
            String clsName = c.getShortName();
            if(javaSourceClassNames.contains(c.isInnerClass() ? clsName.substring(0,clsName.indexOf("$")) : clsName)){
                applicationClass = true;
            }
            if(applicationClass) {
                c.setApplicationClass();
            } else{
                c.setLibraryClass();
            }
        }
        logger.info("Application classes: "+ Scene.v().getApplicationClasses().size());
        logger.info("Library classes: "+ Scene.v().getLibraryClasses().size());
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

    @Override
    public String toString() {
        return apkFile;
    }
}
