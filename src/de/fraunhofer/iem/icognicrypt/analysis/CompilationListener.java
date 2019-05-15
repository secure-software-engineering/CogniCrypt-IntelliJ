package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.actions.IcognicryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.SettingsDialog;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CompilationListener implements ProjectComponent {

    private MessageBusConnection connection;
    private final Project myProject;

    public CompilationListener(Project project) {
        myProject = project;
    }


    @Override
    public void initComponent() {
        connection = myProject.getMessageBus().connect();
        connection.subscribe(CompilerTopics.COMPILATION_STATUS, new CompilationStatusListener() {
            @Override
            public void compilationFinished(boolean aborted, int errors, int warnings, CompileContext compileContext) {

                if (!aborted)
                    startAnalyser(compileContext.getProject());
            }

            @Override
            public void automakeCompilationFinished(int errors, int warnings, CompileContext compileContext) {

                startAnalyser(compileContext.getProject());
            }
        });
    }

    private void startAnalyser(Project project) {

        //Get modules that are present for each project
        for (String moduleCompilePath : CompilerPathsEx.getOutputPaths(ModuleManager.getInstance(project).getModules())) {

            //If compile directory exists, start analysis
            if (Files.isDirectory(Paths.get(moduleCompilePath))) {

                System.out.println("DIR: " + getRulesDirectory());
                AnalysisScanner analyzer = new AnalysisScanner(moduleCompilePath, getRulesDirectory());
                analyzer.runAnalysis();
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
