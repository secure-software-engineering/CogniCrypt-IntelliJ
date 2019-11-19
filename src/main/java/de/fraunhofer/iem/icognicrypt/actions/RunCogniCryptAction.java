package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IIdePlatformProvider;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IdeType;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IProjectOutputFinder;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.analysis.AndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.analysis.IntelliJAnalysis;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import javaLinq.Linq;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.EnumSet;

public class RunCogniCryptAction extends CogniCryptAction implements DumbAware
{
    private static final Logger logger = Logger.getInstance(RunCogniCryptAction.class);
    private final IPersistableCogniCryptSettings _settings;
    private final IdeType _ideType;

    public RunCogniCryptAction()
    {
        super("Run CogniCrypt Analysis...", "Run CogniCrypt Analysis", IconLoader.getIcon("/icons/cognicrypt.png"));
        _settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        _ideType = ServiceManager.getService(IIdePlatformProvider.class).GetRunningPlatform();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getDataContext().getData(PlatformDataKeys.PROJECT);

        if (!CrySLHelper.isValidCrySLRuleDirectory(_settings.getRulesDirectory()))
        {
            NotificationProvider.Warn("No valid CrySL rules found. Please go to \"File > Settings > Other Settings > CogniCrypt\" and select a valid directory.");
            return;
        }

        Iterable<File> files = GetFilesToAnalyze(project);
        /*
        if (files == null || !Linq.any(files))
        {
            NotificationProvider.ShowInfo("No files were analysed");
            return;
        }
         */


        switch (_ideType)
        {
            case IntelliJ:
                IntelliJAnalysis.RunIntelliJAnalysis(project, files);
                break;
            case AndroidStudio:
                AndroidAnalysis.RunAndroidAnalysis(project, files);
                break;
            default:
                NotificationProvider.ShowError("Could not run the Analysis on the current IDE platform");
                break;
        }
    }

    private Iterable<File> GetFilesToAnalyze(Project project)
    {
        IProjectOutputFinder outputFinder = ServiceManager.getService(project, IProjectOutputFinder.class);
        EnumSet<OutputFinderOptions.Flags> options = _settings.GetFindOutputOptions();

        switch (_ideType){

            case Unknown:
                return Collections.EMPTY_LIST;
            case IntelliJ:
                return IntelliJAnalysis.GetFilesForIntelliJ(outputFinder, options);
            case AndroidStudio:
                return AndroidAnalysis.GetFilesForAndroidStudio(outputFinder, options);
        }
        return Collections.EMPTY_LIST;
    }
}


