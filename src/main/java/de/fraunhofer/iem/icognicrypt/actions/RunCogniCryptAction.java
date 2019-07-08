package de.fraunhofer.iem.icognicrypt.actions;

import com.android.tools.idea.sdk.AndroidSdks;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.IdeType;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.AndroidStudioOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.analysis.CogniCryptAndroidStudioAnalysisTask;
import de.fraunhofer.iem.icognicrypt.analysis.JavaProjectAnalysis;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptSettingsPersistentComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RunCogniCryptAction extends CogniCryptAction {

    private static final Logger logger = Logger.getInstance(RunCogniCryptAction.class);

    public RunCogniCryptAction() {
        super("CogniCrypt Analysis","Run CogniCrypt Analysis", IconLoader.getIcon("/icons/cognicrypt.png"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        System.out.println("Action Thread: " + Thread.currentThread().getId());

        Project p = e.getDataContext().getData(PlatformDataKeys.PROJECT);
        RunAnalysis(IdeType.AndroidStudio,p);
    }

    public static void RunAnalysis(IdeType ide, Project project) {
        if(!CogniCryptSettings.isValidCrySLRuleDirectory(getRulesDirectory()))
        {
            Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", "No valid CrySL rules found. Please go to \"File > Settings > Other Settings > CogniCrypt\" and select a valid directory.", NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
            return;
        }
        List<String> classpath = new ArrayList<>();

        //Get modules that are present for each project
        //Get output path for IntelliJ project or APK path in Android Studio
        // TODO: Check if large s/c is required and code can be summarized.
        switch (ide) {
            case AndroidStudio:

                Path platforms = getAndroidPlatformsLocation(project);

                String path = project.getBasePath();
                logger.info("Evaluating compile path "+ path);

                //New FileFinder here
                IOutputFinder outputFinder = AndroidStudioOutputFinder.GetInstance();
                try
                {
                    // TODO: Add build type to settings
                    Iterable<File> files = outputFinder.GetOutputFiles(Paths.get(project.getBasePath()), OutputFinderOptions.AnyBuildType);

                    File projectDir = new File(path);
                    LinkedList<CogniCryptAndroidAnalysis> queue = Lists.newLinkedList();
                    if (!projectDir.exists()) break;

                    //for (File file : FileUtils.listFiles(apkDir, new String[]{"apk"}, true))
                    for (File file : files)
                    {
                        String apkPath = file.getAbsolutePath();
                        logger.info("APK found in " + apkPath);
                        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", "Queing APK " + file.getName() + " for analysis", NotificationType.INFORMATION);
                        Notifications.Bus.notify(notification);
                        Balloon balloon = notification.getBalloon();
                        if (balloon != null) balloon.hide();

                        CogniCryptAndroidAnalysis  analysis = new CogniCryptAndroidAnalysis(apkPath, platforms.toAbsolutePath().toString(), getRulesDirectory(), Lists.newArrayList());
                        queue.add(analysis);
                    }
                    if (queue.isEmpty())
                        Notifications.Bus.notify(new Notification("CogniCrypt", "Warning", "No APK file detected. Run Build > Make Project assemble an APK and trigger the analysis again.", NotificationType.WARNING));
                    else
                    {
                        ProgressManager.getInstance().run(new CogniCryptAndroidStudioAnalysisTask(project, queue));
                    }

                }
                // TODO: There should be a custom exception handling for the tool at some time (GUI, Report, etc.)
                catch (CogniCryptException e)
                {
                    logger.info("CogniCryptException was thrown: " + e.getMessage());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    logger.info("Exception was thrown: " + e.getMessage());
                    e.printStackTrace();
                }
                break;
            case IntelliJ:

                for (Module module : ModuleManager.getInstance(project).getModules()) {

                    //Add classpath jars from Java, Android and Gradle to list
                    for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots()) {
                        classpath.add(file.getPath());
                    }
                    String modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
                    logger.info("Module Output Path "+ modulePath);
                    Task analysis = new JavaProjectAnalysis(modulePath, Joiner.on(":").join(classpath), getRulesDirectory());
                    ProgressManager.getInstance().run(analysis);
                }
                break;
        }

    }

    private static Path getAndroidPlatformsLocation(Project project) {
        File androidSdkPath = AndroidSdks.getInstance().findPathOfSdkWithoutAddonsFolder(project);
        String android_sdk_root;

        if (androidSdkPath != null) {
            android_sdk_root = androidSdkPath.getAbsolutePath();
            logger.info("Choosing android sdk path automatically");
        }
        else {
            android_sdk_root = System.getenv(Constants.ANDROID_SDK);
            logger.info("Fallback for android sdk path to environment variable");
        }

        Path sdkPath = Paths.get(android_sdk_root);
        if (android_sdk_root == null || android_sdk_root.equals("") || !sdkPath.toFile().exists())
            throw new RuntimeException("Environment variable "+Constants.ANDROID_SDK+" not found!");
        return sdkPath.resolve("platforms");
    }

    public static String getRulesDirectory() {
        CogniCryptSettingsPersistentComponent settings = CogniCryptSettingsPersistentComponent.getInstance();
        return settings.getRulesDirectory();
    }
}


