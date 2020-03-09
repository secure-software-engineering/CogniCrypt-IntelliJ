package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.callgraph.ObservableDynamicICFG;
import boomerang.callgraph.ObservableICFG;
import boomerang.preanalysis.BoomerangPretransformer;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.CryptoScanner;
import crypto.rules.CrySLRule;
import crypto.rules.CrySLRuleReader;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import soot.*;
import soot.options.Options;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CryptoAnalysisWrapper
{
    private String _applicationPath;
    private String _fullClassPath;
    private CrySLAnalysisListener _listener;
    private String _rulesDirectory;

    private CryptoAnalysisWrapper(String applicationPath, String fullClassPath, CrySLAnalysisListener listener){
        _applicationPath = applicationPath;
        _fullClassPath = fullClassPath;
        _listener = listener;
        IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        _rulesDirectory = settings.getRulesDirectory();
    }

    public static void RunAnalysis(String applicationPath, String fullClassPath, Project project) {
        AnalysisListenerService listener = ServiceManager.getService(project, AnalysisListenerService.class);
        RunAnalysis(applicationPath, fullClassPath, listener);
    }

    public static void RunAnalysis(String applicationPath, String fullClassPath, CrySLAnalysisListener listener)
    {
        CryptoAnalysisWrapper wrapper = new CryptoAnalysisWrapper(applicationPath, fullClassPath, listener);
        wrapper.RunAnalysis();
    }

    private void RunAnalysis(){
        try
        {
            G.v();
            G.reset();
            setSootOptions();
            registerTransformers();
            runSoot();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void setSootOptions()
    {
        Options.v().set_soot_classpath(_fullClassPath);
        Options.v().set_process_dir(Arrays.asList(this._applicationPath.split(File.pathSeparator)));
        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_include(getIncludeList());
        Options.v().set_exclude(getExcludeList());

        Scene.v().loadNecessaryClasses();

        Options.v().setPhaseOption("cg.cha", "on");
        Options.v().setPhaseOption("cg", "all-reachable:true");
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_output_format(Options.output_format_none);
    }

    /*
    private static String getSootClasspath()
    {
        Collection<String> applicationClassPath = applicationClassPath();
        Collection<String> libraryClassPath = libraryClassPath();

        libraryClassPath.addAll(applicationClassPath);
        System.out.println(Joiner.on(File.pathSeparator).join(libraryClassPath));
        return Joiner.on(File.pathSeparator).join(libraryClassPath);
    }

    private static Collection<String> libraryClassPath() {
        Collection<String> libraryClassPath = Sets.newHashSet();


        libraryClassPath.add("D:\\Java\\java-1.8.0-openjdk-1.8.0.212-1.b04.ojdkbuild.windows.x86_64\\jre\\lib\\rt.jar");
        //libraryClassPath.add("VIRTUAL_FS_FOR_JDK");
        //libraryClassPath.add("D:\\Java\\java-11-openjdk-11.0.4.11-1.windows.ojdkbuild.x86_64\\lib\\modules");

        return libraryClassPath;
    }
     */

    private void runSoot()
    {
        PackManager.v().getPack("cg").apply();
        PackManager.v().getPack("wjtp").apply();
    }

    private void registerTransformers()
    {
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", createAnalysisTransformer()));
    }

    private SceneTransformer createAnalysisTransformer() {
        return new SceneTransformer() {

            @Override
            protected void internalTransform(final String phaseName, final Map<String, String> options) {
                BoomerangPretransformer.v().apply();
                final ObservableDynamicICFG icfg = new ObservableDynamicICFG(true);
                CryptoScanner scanner = new CryptoScanner() {

                    @Override
                    public ObservableICFG<Unit, SootMethod> icfg() {
                        return icfg;
                    }
                };
                scanner.getAnalysisListener().addReportListener(_listener);
                scanner.scan(getRules());
            }
        };
    }

    private List<CrySLRule> getRules()
    {
        return CrySLRuleReader.readFromDirectory(new File(_rulesDirectory));
    }

    private static List<String> getIncludeList() {
        final List<String> includeList = new LinkedList<String>();
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

    private List<String> getExcludeList() {
        final List<String> excludeList = new LinkedList<String>();
        for (final CrySLRule r : getRules()) {
            excludeList.add(crypto.Utils.getFullyQualifiedName(r));
        }
        return excludeList;
    }
}
