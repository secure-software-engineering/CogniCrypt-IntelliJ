package de.fraunhofer.iem.icognicrypt.analysis;

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
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;

import java.io.File;

public class IntelliJAnalysis
{
    private static final Logger logger = Logger.getInstance(IntelliJAnalysis.class);

    public static void RunIntelliJAnalysis(Project project, Iterable<File> filesToAnalyze)
    {
        logger.info("Running IntelliJ Analysis");

        IdeCryptoScanner.RunAnalysis(project);

        /*
        for (Module module : ModuleManager.getInstance(project).getModules())
        {
            //Add classpath jars from Language, Android and Gradle to list
            for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots())
            {
            }

            String modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
            logger.info("Module Output Path " + modulePath);
            IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);

            String fullClasspath = OrderEnumerator.orderEntries(module).recursively().getPathsList().getPathsString();
            System.out.println(fullClasspath);
            Task analysis = new JavaProjectAnalysisTask(modulePath, fullClasspath, settings.getRulesDirectory());



            //ProgressManager.getInstance().run(analysis);
        }
         */
    }
}
