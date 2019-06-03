package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import crypto.HeadlessCryptoScanner;
import crypto.analysis.CrySLAnalysisListener;
import de.fraunhofer.iem.icognicrypt.results.AnalysisListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JavaProjectAnalysis extends Task.Backgroundable {

    protected final String applicationClassPath;
    protected final String wholeClassPath;
    protected final String rulesDirectory;

    JavaProjectAnalysis(String applicationClass , String classPath, String rulesDir) {
        super(null, "Performing CogniCrypt Analysis");
        this.applicationClassPath = applicationClass;
        this.wholeClassPath = classPath;
        this.rulesDirectory = rulesDir;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        HeadlessCryptoScanner scanner = new HeadlessCryptoScanner() {
            @Override
            protected String getCSVOutputFile() {
                return null;
            }


            @Override
            protected String sootClassPath() {
                return applicationClassPath;
            }

            @Override
            protected String applicationClassPath() {
                return wholeClassPath;
            }

            @Override
            protected String softwareIdentifier() {
                return null;
            }

            @Override
            protected CrySLAnalysisListener getAdditionalListener() {
                return new AnalysisListener();
            }

            @Override
            protected String getRulesDirectory() {
                return rulesDirectory;
            }
        };
        scanner.exec();
    }
}
