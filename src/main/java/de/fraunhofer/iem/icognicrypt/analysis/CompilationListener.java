package de.fraunhofer.iem.icognicrypt.analysis;

import com.android.tools.idea.gradle.project.build.GradleBuildContext;
import com.android.tools.idea.gradle.project.build.invoker.GradleInvocationResult;
import com.android.tools.idea.project.AndroidProjectBuildNotifications;
import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
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

                        //startAnalyser(IdeType.AndroidStudio, project);
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
//                if (!aborted)
//                    startAnalyser(IdeType.IntelliJ, compileContext.getProject());
            }

            @Override
            public void automakeCompilationFinished(int errors, int warnings, CompileContext compileContext) {
                //startAnalyser(IdeType.IntelliJ, compileContext.getProject());
            }
        });
    }

    public void disposeComponent() {
        if(connection != null) {
            connection.disconnect();
        }
    }
}
