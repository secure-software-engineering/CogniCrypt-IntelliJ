package de.fraunhofer.iem.icognicrypt.analysis;

import crypto.HeadlessCryptoScanner;
import crypto.analysis.CrySLAnalysisListener;
import de.fraunhofer.iem.icognicrypt.results.AnalysisListener;

class AnalysisScanner {

    private String projectPath;
    private String rulesDirectory;

    AnalysisScanner(String classPath, String rulesDir) {
        projectPath = classPath;
        rulesDirectory = rulesDir;
    }

    void runAnalysis() {

        HeadlessCryptoScanner scanner = createAnalysisFor(projectPath, projectPath, rulesDirectory);
        scanner.exec();
    }

    private HeadlessCryptoScanner createAnalysisFor(String applicationClassPath, String sootClassPath, String rulesDir) {

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
                return sootClassPath;
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
                return rulesDir;
            }
        };

        return scanner;
    }
}
