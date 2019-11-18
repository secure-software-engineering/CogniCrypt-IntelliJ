package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IProjectOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.android.AndroidPlatformLocator;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import de.fraunhofer.iem.icognicrypt.ui.multipleOutputFilesDialog.MultipleOutputFilesDialog;
import javaLinq.Linq;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class AndroidAnalysis
{
    private static final Logger logger = Logger.getInstance(AndroidAnalysis.class);

    public static void RunAndroidAnalysis(Project project, Iterable<File> filesToAnalyze)
    {
        logger.info("Running Android Analysis");

        Path androidPlatformPath = AndroidPlatformLocator.getAndroidPlatformsLocation(project).toAbsolutePath();
        String projectBasePath = project.getBasePath();

        logger.info("Evaluating compile path " + projectBasePath);

        File projectDir = new File(projectBasePath);
        if (!projectDir.exists())
            return;

        LinkedList<CogniCryptAndroidAnalysis> queue = Lists.newLinkedList();

        for (File file : filesToAnalyze)
        {
            String apkPath = file.getAbsolutePath();
            logger.info("APK found in " + apkPath);

            IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
            CogniCryptAndroidAnalysis analysis = new CogniCryptAndroidAnalysis(apkPath, androidPlatformPath.toString(), settings.getRulesDirectory());
            queue.add(analysis);
        }
        if (queue.isEmpty())
            NotificationProvider.ShowInfo("No APK file detected. Run Build > Make Project assemble an APK and trigger the analysis again.");
        else
            ProgressManager.getInstance().run(new CogniCryptAndroidStudioAnalysisTask(project, queue));
    }

    public static Iterable<File> GetFilesForAndroidStudio(IProjectOutputFinder outputFinder, Set<OutputFinderOptions.Flags> options)
    {
        // If not options are given we directly show the 'Select Files' dialog.
        if (options == null || !Linq.any(options))
            return outputFinder.GetOutputFilesFromDialog();

        // Get possible files
        Iterable<File> files = outputFinder.GetOutputFiles(options);

        boolean dialogFlag = false;
        boolean abortFlag = false;

        // If there are multiple files, ask the user which ones to analyse
        if (Linq.count(files) > 1)
        {
            MultipleOutputFilesDialog dialog = new MultipleOutputFilesDialog(files, outputFinder.GetCache().GetCachedMultipleFileSelection());
            MultipleOutputFilesDialog.OutputFilesDialogResult result = dialog.ShowDialog();

            if (result == MultipleOutputFilesDialog.OutputFilesDialogResult.ChooseManually)
                dialogFlag = true;
            else if (result == MultipleOutputFilesDialog.OutputFilesDialogResult.OK)
            {
                files = dialog.GetSelectedFiles();
                if (dialog.GetSaveChoice())
                    outputFinder.GetCache().SetMultipleFileSelection(files);
                else
                    outputFinder.GetCache().InvalidateMultipleSelectedFiles();
            }
            else{
                files = Collections.EMPTY_LIST;
                abortFlag = true;
            }
        }

        // If the user requested to choose the files manually, or no files have been found but the procedure was not aborted
        // show the 'Select File' dialog
        if (dialogFlag || (!Linq.any(files) && Linq.any(options))  && !abortFlag)
        {
            files = outputFinder.GetOutputFilesFromDialog(!dialogFlag);
        }

        return files;
    }
}
