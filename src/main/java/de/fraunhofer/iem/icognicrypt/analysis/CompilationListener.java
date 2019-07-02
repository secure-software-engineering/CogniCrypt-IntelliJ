package de.fraunhofer.iem.icognicrypt.analysis;

import com.android.tools.idea.gradle.project.build.GradleBuildContext;
import com.android.tools.idea.gradle.project.build.invoker.GradleInvocationResult;
import com.android.tools.idea.project.AndroidProjectBuildNotifications;
import com.android.tools.idea.sdk.AndroidSdks;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptSettingsPersistentComponent;
import de.fraunhofer.iem.icognicrypt.IdeSupport.IdeType;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.AndroidStudioOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IOutputFinder;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationListener implements ProjectComponent {

    private MessageBusConnection connection;
    private final Project project;
    private static final Logger logger = Logger.getInstance(CompilationListener.class);

    public CompilationListener(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {

        logger.info("Initializing ICogniCrypt");

        if(!Constants.AUTOMATIC_SCAN_ON_COMPILE){
            return;
        }

        /*
            Listens for Build notifications in Android Studio.
        */
        AndroidProjectBuildNotifications.subscribe(project, context -> {


            if (context instanceof GradleBuildContext) {

                GradleBuildContext gradleBuildContext = (GradleBuildContext) context;
                GradleInvocationResult build = gradleBuildContext.getBuildResult();

                if (build.isBuildSuccessful()) {

                    for (String buildTask : build.getTasks()) {

                        if (buildTask.equals("clean"))
                            continue;

                        startAnalyser(IdeType.AndroidStudio, project);
                    }
                }
            }
        });


        /*
            Listens for Build notifications in IntelliJ.
        */
        connection = project.getMessageBus().connect();
        connection.subscribe(CompilerTopics.COMPILATION_STATUS, new CompilationStatusListener() {

            @Override
            public void compilationFinished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
                if (!aborted)
                    startAnalyser(IdeType.IntelliJ, compileContext.getProject());
            }

            @Override
            public void automakeCompilationFinished(int errors, int warnings, CompileContext compileContext) {
                startAnalyser(IdeType.IntelliJ, compileContext.getProject());
            }
        });
    }

    public static void startAnalyser(IdeType ide, Project project) {
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
                       List<String> javaSourceFiles = FileUtils.listFiles(projectDir, new String[]{"java"}, true).stream().map(f -> f.getName().replace(".java","")).distinct().collect(Collectors.toList());
                        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", "Queing APK " + file.getName() + " for analysis", NotificationType.INFORMATION);
                        Notifications.Bus.notify(notification);
                        Balloon balloon  = notification.getBalloon();
                        if (balloon != null)
                            balloon.hide();

                        CogniCryptAndroidAnalysis analysis = new CogniCryptAndroidAnalysis(apkPath, platforms.toAbsolutePath().toString(), getRulesDirectory(), Lists.newArrayList());
                        queue.add(analysis);
                    }
                    if (queue.isEmpty())
                        Notifications.Bus.notify(new Notification("CogniCrypt", "Warning", "No APK file detected. Run Build > Make Project assemble an APK and trigger the analysis again.", NotificationType.WARNING));
                    else ProgressManager.getInstance().run(new AndroidProjectAnalysisQueue(project, queue));

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

    public void disposeComponent() {
        if(connection != null) {
            connection.disconnect();
        }
    }
}
