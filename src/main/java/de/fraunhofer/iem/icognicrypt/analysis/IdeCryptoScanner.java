package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.callgraph.ObservableDynamicICFG;
import boomerang.callgraph.ObservableICFG;
import boomerang.preanalysis.BoomerangPretransformer;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.CrySLRulesetSelector;
import crypto.analysis.CryptoScanner;
import crypto.rules.CryptSLRule;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.options.Options;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

public class IdeCryptoScanner
{
    private static final Logger logger = LoggerFactory.getLogger(AnalysisListener.class);

    public static void RunAnalysis(Project project)
    {
        try
        {
            G.reset();
            setOptions(project);
            registerTransformers(new AnalysisListener());
            RunAnalysis();
        }
        catch (Exception e){
            System.out.println(e);
            logger.error(e.getLocalizedMessage());
        }

    }

    private static void RunAnalysis()
    {
        PackManager.v().getPack("cg").apply();
    }

    private static void registerTransformers(final CrySLAnalysisListener resultsReporter) {
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", createAnalysisTransformer(resultsReporter)));
    }

    private static List<CryptSLRule> getRules()
    {
        IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        String rulesDirectory = settings.getRulesDirectory();
        return CrySLRulesetSelector.makeFromPath(new File(rulesDirectory), CrySLRulesetSelector.RuleFormat.BINARY);
    }

    private static void setOptions(Project project)
    {
        Options.v().set_soot_classpath(getSootClasspath(project));
        Options.v().set_soot_modulepath("VIRTUAL_FS_FOR_JDK");

        Options.v().set_process_dir(applicationClassPath(project));

        Options.v().set_keep_line_number(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_include(getIncludeList());
        Options.v().set_exclude(getExcludeList());
        Scene.v().loadNecessaryClasses();
        Options.v().setPhaseOption("cg.cha", "on");
        Options.v().setPhaseOption("cg", "all-reachable:true");
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_output_format(Options.output_format_none);
    }

    private static List<String> applicationClassPath(Project project)
    {
        List<String> paths = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(project).getModules())
        {
            String modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
            paths.add(modulePath);
        }
        return paths;
    }

    private static String getSootClasspath(Project project)
    {
        Collection<String> applicationClassPath = applicationClassPath(project);
        Collection<String> libraryClassPath = libraryClassPath(project);
        libraryClassPath.addAll(applicationClassPath);
        System.out.println(Joiner.on(File.pathSeparator).join(libraryClassPath));
        return Joiner.on(File.pathSeparator).join(libraryClassPath);
    }

    private static Collection<String> libraryClassPath(Project project)
    {
        Collection<String> libraryClassPath = Sets.newHashSet();

        String modulePath = "D:/Java/java-11-openjdk-11.0.3.7-1.windows.ojdkbuild.x86_64/lib/jrt-fs.jar";


        File f = new File("VIRTUAL_FS_FOR_JDK");

        String p = f.getAbsolutePath();

        /*
        for (Module module : ModuleManager.getInstance(project).getModules())
        {
            PathsList fullClasspathList = OrderEnumerator.orderEntries(module).recursively().getPathsList();

            List<VirtualFile> f = fullClasspathList.getRootDirs();

            System.out.println(f);

        }

         */

        return libraryClassPath;
    }

    private static SceneTransformer createAnalysisTransformer(final CrySLAnalysisListener resultsReporter) {
        return new SceneTransformer() {
            @Override
            protected void internalTransform(String s, Map<String, String> map)
            {
                BoomerangPretransformer.v().apply();
                ObservableDynamicICFG icfg = new ObservableDynamicICFG(true);

                CryptoScanner scanner = new CryptoScanner() {
                    @Override
                    public ObservableICFG<Unit, SootMethod> icfg()
                    {
                        return icfg;
                    }
                };
                scanner.getAnalysisListener().addReportListener(resultsReporter);
                scanner.scan(getRules());
            }
        };
    }

    private static List<String> getIncludeList() {
        final List<String> includeList = new LinkedList<>();
        includeList.add("java.lang.AbstractStringBuilder");
        includeList.add("java.lang.Boolean");
        includeList.add("java.lang.Byte");
        includeList.add("java.lang.Class");
        includeList.add("java.lang.Integer");
        includeList.add("java.lang.Long");
        includeList.add("java.lang.Object");
        includeList.add("java.lang.String");
        includeList.add("java.lang.StringCoding");
        includeList.add("java.lang.StringIndexOutOfBoundsException");
        return includeList;
    }

    private static List<String> getExcludeList() {
        final List<String> excludeList = new LinkedList<>();
        for (final CryptSLRule r : getRules()) {
            excludeList.add(crypto.Utils.getFullyQualifiedName(r));
        }
        return excludeList;
    }
}
