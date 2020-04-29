package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class JavaProjectAnalysisTask extends Task.Backgroundable {

    private final String _applicationClassPath;
    private final String _wholeClassPath;

    public JavaProjectAnalysisTask(Project project, String applicationPath , String classPath) {
        super(project, "Performing CogniCrypt Analysis");
        _applicationClassPath = applicationPath;
        _wholeClassPath = classPath;
    }


    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        CryptoAnalysisWrapper.RunAnalysis(_applicationClassPath, _wholeClassPath, myProject);
    }
}
