package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.CryptoScanner;
import org.jetbrains.annotations.NotNull;

public class JavaProjectAnalysisTask extends Task.Backgroundable {

    private final String _applicationClassPath;
    private final String _wholeClassPath;

    public JavaProjectAnalysisTask(String applicationPath , String classPath) {
        super(null, "Performing CogniCrypt Analysis");
        this._applicationClassPath = applicationPath;
        this._wholeClassPath = classPath;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        CryptoAnalysisWrapper.RunAnalysis(_applicationClassPath, _wholeClassPath);
    }
}
