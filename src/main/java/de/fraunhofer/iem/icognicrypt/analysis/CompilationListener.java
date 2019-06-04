package de.fraunhofer.iem.icognicrypt.analysis;

import com.android.tools.idea.gradle.project.build.GradleBuildContext;
import com.android.tools.idea.gradle.project.build.invoker.GradleInvocationResult;
import com.android.tools.idea.project.AndroidProjectBuildNotifications;
import com.android.tools.idea.sdk.AndroidSdks;
import com.google.common.base.Joiner;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.actions.IcognicryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.SettingsDialog;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

                        startAnalyser(Constants.IDE_ANDROID_STUDIO, project);
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
                    startAnalyser(Constants.IDE_INTELLIJ, compileContext.getProject());
            }

            @Override
            public void automakeCompilationFinished(int errors, int warnings, CompileContext compileContext) {

                startAnalyser(Constants.IDE_INTELLIJ, compileContext.getProject());
            }
        });
    }

    public static void startAnalyser(int IDE, Project project) {

        String modulePath = "";
        List<String> classpath = new ArrayList<>();

        //Get modules that are present for each project
        //Get output path for IntelliJ project or APK path in Android Studio
        switch (IDE) {
            case Constants.IDE_ANDROID_STUDIO:

                String path = project.getBasePath();
                logger.info("Evaluating compile path "+ path);

                File apkDir = new File(path);

                if (apkDir.exists()) {

                    for (File file : FileUtils.listFiles(apkDir, new String[]{"apk"}, true)) {

                        logger.info("Evaluating file "+ file.getName());
                        modulePath = file.getAbsolutePath();
                        logger.info("APK found in {} "+ modulePath);

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

                        if (android_sdk_root == null || "".equals(android_sdk_root))
                            throw new RuntimeException("Environment variable "+Constants.ANDROID_SDK+" not found!");
                        Path platforms = Paths.get(android_sdk_root).resolve("platforms");
                        Task analysis = new AndroidProjectAnalysis(modulePath, platforms.toAbsolutePath().toString(), getRulesDirectory());
                        ProgressManager.getInstance().run(analysis);
                    }
                }

                break;
            case Constants.IDE_INTELLIJ:

                for (Module module : ModuleManager.getInstance(project).getModules()) {

                    //Add classpath jars from Java, Android and Gradle to list
                    for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots()) {
                        classpath.add(file.getPath());
                    }
                    modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
                    logger.info("Module Output Path "+ modulePath);
                    Task analysis = new JavaProjectAnalysis(modulePath, Joiner.on(":").join(classpath), getRulesDirectory());
                    ProgressManager.getInstance().run(analysis);
                }
                break;
        }

    }

    public static String getRulesDirectory() {

        IcognicryptSettings settings = IcognicryptSettings.getInstance();
        File rules = new File(settings.getRulesDirectory());

        //TODO Check if directory contains correct files
        if (rules.exists())
            return settings.getRulesDirectory();
        else {
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.pack();
            settingsDialog.setSize(550, 150);
            settingsDialog.setLocationRelativeTo(null);
            settingsDialog.setVisible(true);

            return settings.getRulesDirectory();
        }
    }

    public void disposeComponent() {
        connection.disconnect();
    }
}
