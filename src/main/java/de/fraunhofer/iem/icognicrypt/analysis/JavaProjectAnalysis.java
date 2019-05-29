package de.fraunhofer.iem.icognicrypt.analysis;

import crypto.HeadlessCryptoScanner;
import crypto.analysis.CrySLAnalysisListener;
import de.fraunhofer.iem.icognicrypt.results.AnalysisListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JavaProjectAnalysis implements Runnable {

    protected final String applicationClassPath;
    protected final String wholeClassPath;
    protected final String rulesDirectory;

    JavaProjectAnalysis(String applicationClass , String classPath, String rulesDir) {
        this.applicationClassPath = applicationClass;
        this.wholeClassPath = classPath;
        this.rulesDirectory = rulesDir;
    }

    public void run() {
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
