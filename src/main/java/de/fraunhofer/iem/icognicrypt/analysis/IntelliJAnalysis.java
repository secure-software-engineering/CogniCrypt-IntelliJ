package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Joiner;
import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.LanguageLevelUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.java.LanguageLevel;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IProjectOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import org.eclipse.jdt.internal.compiler.ProcessTaskManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IntelliJAnalysis
{
    private static final Logger logger = Logger.getInstance(IntelliJAnalysis.class);

    public static void RunIntelliJAnalysis(Project project, Iterable<File> filesToAnalyze)
    {
        logger.info("Running IntelliJ Analysis");

        int runningVersion = getRunningVersion();
        int targetVersion = 11;
        if (!analysisSupported(runningVersion, targetVersion))
        {
            MessageBox.Show("Unfortunately, running the analysis with your current environment and program code is not possible.\r\n" +
                                    "Your IDE is running Java " + runningVersion + " but your application targets Java " + targetVersion + ". " +
                    "Imagine a boundary between Java 8 and Java 9. For successfully running an analysis, both version numbers must be on the same side of the boundary.\r\n\r\n " +
                                    "We are working for adding compatibility to this setting.\r\n\r\n" +
                                    "To temporarily avoid the issue, you could adapt the application target runtime or download IntelliJ with JBR 8:\r\n" +
                    "https://www.jetbrains.com/idea/download/other.html", MessageBox.MessageBoxButton.OK, MessageBox.MessageBoxType.Information);
            return;
        }


        // RunConfiguration rc = RunManager.getInstance(project).getSelectedConfiguration().getConfiguration();
        // ApplicationConfiguration apc = (ApplicationConfiguration) rc;

        for (Module module : ModuleManager.getInstance(project).getModules())
        {
            LanguageLevel lang = LanguageLevelUtil.getEffectiveLanguageLevel(module);
            lang = null;


            List<String> classpath = new ArrayList<>();

            //Add classpath jars from Language, Android and Gradle to list
            for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots())
            {
                classpath.add(file.getPath());
            }

            String fullClasspath = OrderEnumerator.orderEntries(module).recursively().getPathsList().getPathsString();
            fullClasspath = null;
            //String modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
            String modulePath = CompilerPaths.getModuleOutputPath(module, false);

            logger.info("Module Output Path " + modulePath);

            String fullCp = Joiner.on(File.pathSeparator).join(classpath);
            logger.info(fullCp);

            ProgressManager.getInstance().run(new JavaProjectAnalysisTask("C:\\Users\\lrs\\IdeaProjects\\helloworld\\out\\production\\helloworld",
                                                                          "D:\\Java\\java-1.8.0-openjdk-1.8.0.212-1.b04.ojdkbuild.windows.x86_64\\jre\\lib\\rt.jar;C:\\Users\\lrs\\IdeaProjects\\helloworld\\out\\production\\helloworld"));

            return;

            /*

            IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);

            Task analysis = new JavaProjectAnalysisTask(modulePath, Joiner.on(File.pathSeparator).join(classpath),
                                                        settings.getRulesDirectory());
            ProgressManager.getInstance().run(analysis);

             */
        }
    }

    private static boolean analysisSupported(int runningVersion, int targetVersion)
    {
        if (runningVersion < 9 && targetVersion < 9 || runningVersion > 8 && targetVersion > 8)
            return true;
        return false;
    }

    private static int getRunningVersion() {
        String version = System.getProperty("java.version");
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
    }

    public static Iterable<File> GetFilesForIntelliJ(IProjectOutputFinder outputFinder, Set<OutputFinderOptions.Flags> options)
    {
        return null;
    }
}
