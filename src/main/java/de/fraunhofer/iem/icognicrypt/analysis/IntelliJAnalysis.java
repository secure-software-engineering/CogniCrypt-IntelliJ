package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Joiner;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.vfs.VirtualFile;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IProjectOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IntelliJAnalysis
{
    private static final Logger logger = Logger.getInstance(IntelliJAnalysis.class);

    public static void RunIntelliJAnalysis(Project project, Iterable<File> filesToAnalyze) throws CogniCryptException
    {
        logger.info("Running IntelliJ Analysis");

        if (!analysisSupported())
            throw new CogniCryptException("");


        for (Module module : ModuleManager.getInstance(project).getModules())
        {
            List<String> classpath = new ArrayList<>();

            //Add classpath jars from Language, Android and Gradle to list
            for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots())
            {
                classpath.add(file.getPath());
            }
            String modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
            logger.info("Module Output Path " + modulePath);

            /*

            IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);

            Task analysis = new JavaProjectAnalysisTask(modulePath, Joiner.on(File.pathSeparator).join(classpath),
                                                        settings.getRulesDirectory());
            ProgressManager.getInstance().run(analysis);

             */
        }
    }

    private static boolean analysisSupported()
    {
        return false;
    }

    private static int getVersion() {
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
