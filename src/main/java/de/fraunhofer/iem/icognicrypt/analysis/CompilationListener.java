package de.fraunhofer.iem.icognicrypt.analysis;

import com.android.tools.idea.gradle.project.build.GradleBuildContext;
import com.android.tools.idea.gradle.project.build.invoker.GradleInvocationResult;
import com.android.tools.idea.project.AndroidProjectBuildNotifications;
import com.google.common.base.Joiner;
import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.actions.IcognicryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.SettingsDialog;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompilationListener implements ProjectComponent {

    private MessageBusConnection connection;
    private final Project project;
    private static final Logger logger = LoggerFactory.getLogger(CompilationListener.class);

    public CompilationListener(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {

        /*
            Listens for Build notifications in Android Studio.
        */
        AndroidProjectBuildNotifications.subscribe(project, context -> {

            logger.info("Call Source {} buildComplete");

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
                logger.info("Call Source {} compilationFinished");

                if (!aborted)
                    startAnalyser(Constants.IDE_INTELLIJ, compileContext.getProject());
            }

            @Override
            public void automakeCompilationFinished(int errors, int warnings, CompileContext compileContext) {

                startAnalyser(Constants.IDE_INTELLIJ, compileContext.getProject());
            }
        });
    }

    private void startAnalyser(int IDE, Project project) {

        String modulePath = "";
        List<String> classpath = new ArrayList<>();

        //Get modules that are present for each project
        for (Module module : ModuleManager.getInstance(project).getModules()) {

            logger.info("Checking {} module, type={}", module.getName(), module.getModuleTypeName());

            //Add classpath jars from Java, Android and Gradle to list
            for (VirtualFile file : OrderEnumerator.orderEntries(module).recursively().getClassesRoots()) {
                classpath.add(file.getPath());
            }

            StaticAnalysis analysis;
            //Get output path for IntelliJ project or APK path in Android Studio
            switch (IDE) {
                case Constants.IDE_ANDROID_STUDIO:

                    String path = project.getBasePath();
                    logger.info("Evaluating compile path {}", path);

                    File apkDir = new File(path);

                    if (apkDir.exists()) {

                        for (File file : FileUtils.listFiles(apkDir, new String[]{"apk"}, true)) {

                            logger.info("Evaluating file {}", file.getName());

                            if (file.getAbsolutePath().contains("debug")) {
                                modulePath = file.getAbsolutePath();
                                logger.info("APK found in {} ", modulePath);

                                String android_sdk_root = System.getProperty("ANDROID_SDK_ROOT");
                                android_sdk_root = "C:\\Android\\sdk\\platforms";
                                if(android_sdk_root == null || "".equals(android_sdk_root)){
                                    throw new RuntimeException("Environment variable ANDROID_SDK_ROOT not set!");
                                }
                                analysis = new AndroidProjectAnalysis(modulePath, android_sdk_root, getRulesDirectory());
                                analysis.run();
                            }
                        }
                    }

                    break;
                case Constants.IDE_INTELLIJ:
                    modulePath = CompilerPathsEx.getModuleOutputPath(module, false);
                    logger.info("Module Output Path {} ", modulePath);
                    analysis = new JavaProjectAnalysis(modulePath, Joiner.on(":").join(classpath), getRulesDirectory());
                    analysis.run();
                    break;
            }

        }
    }

    private String getRulesDirectory() {

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
