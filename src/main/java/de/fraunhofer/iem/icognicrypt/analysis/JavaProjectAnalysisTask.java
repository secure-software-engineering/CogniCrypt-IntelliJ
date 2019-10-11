package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import crypto.HeadlessCryptoScanner;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.CrySLRulesetSelector;
import crypto.rules.CryptSLRule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class JavaProjectAnalysisTask extends Task.Backgroundable {

    protected final String applicationClassPath;
    protected final String wholeClassPath;
    protected final String rulesDirectory;

    public JavaProjectAnalysisTask(String applicationClass , String classPath, String rulesDir) {
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
            protected List<CryptSLRule> getRules()
            {
                return CrySLRulesetSelector.makeFromPath(new File(rulesDirectory), CrySLRulesetSelector.RuleFormat.BINARY);
            }
        };
        try
        {
            scanner.exec();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
