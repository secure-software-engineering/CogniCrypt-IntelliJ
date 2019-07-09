package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import crypto.analysis.errors.AbstractError;
import crypto.analysis.errors.IncompleteOperationError;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.core.Java.JavaFileToClassNameResolver;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.results.*;
import de.fraunhofer.iem.icognicrypt.ui.ToolWindow.ICogniCryptToolWindowManager;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import de.fraunhofer.iem.icognicrypt.ui.ToolWindow.ToolWindowModelType;
import org.jetbrains.annotations.NotNull;
import soot.G;
import soot.SootClass;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CogniCryptAndroidStudioAnalysisTask extends Task.Backgroundable{

    private static final Logger logger = Logger.getInstance(CogniCryptAndroidStudioAnalysisTask.class);
    private ICogniCryptResultTableModel _tableModel;
    private Stopwatch _stopWatch;
    private Queue<CogniCryptAndroidAnalysis> _analysisQueue;

    private int _analysedFilesCount;

    private final List<String> sourceCodeJavaFiles;

    private IResultProvider _resultProvider;

    public CogniCryptAndroidStudioAnalysisTask(Project p, Queue<CogniCryptAndroidAnalysis> analysisQueue){
        super(p, "Performing CogniCrypt Analysis");
        _analysisQueue = analysisQueue;

        ICogniCryptToolWindowManager toolWindowManager = ServiceManager.getService(p, ICogniCryptToolWindowManager.class);
        _resultProvider = ServiceManager.getService(p, IResultProvider.class);

        try
        {
            _tableModel =  toolWindowManager.GetModel(ToolWindowModelType.Results, ICogniCryptResultWindow.class).GetTableModel();
        }
        catch (CogniCryptException e)
        {
            logger.error(e);
        }

        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY) {
            sourceCodeJavaFiles = JavaFileToClassNameResolver.findFullyQualifiedJavaClassNames(getProject());
        } else {
            sourceCodeJavaFiles = Lists.newArrayList();
        }

        _stopWatch = Stopwatch.createUnstarted();
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {

        // TODO: Check on a few relevant points (maybe after each .apk analysis) if the process was canceled by the user

        // Remove errors before rerunning Cognicrypt
        _resultProvider.RemoveAllResults();

        int size = _analysisQueue.size();

        _stopWatch.start();
        while(!_analysisQueue.isEmpty())
        {
            _analysedFilesCount++;
            indicator.setText(String.format("Performing CogniCrypt Analysis (APK %s of %s)", _analysedFilesCount, size));
            G.v().reset();
            CogniCryptAndroidAnalysis curr = _analysisQueue.poll();

            try {
                Collection<AbstractError> results = curr.run();

                for(AbstractError abstractError : results){
                    if (abstractError.getErrorLocation().getUnit().isPresent()) {

                        if(isIgnoredErrorType(abstractError))
                            continue;
                        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();

                        String name = affectedClass.getName();
                        int line = abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber() - 1;

                        _resultProvider.AddResult(name, line, new CogniCryptError(abstractError.toErrorMarkerString(), name, line));
                    }
                }
            } catch (Throwable e){
                Notification notification = new Notification("CogniCrypt", "CogniCrypt", String.format("Crashed on %s", curr), NotificationType.INFORMATION);
                logger.error(e);
                Notifications.Bus.notify(notification);
            }
            indicator.setFraction((_analysedFilesCount / (double)size));
        }
        _stopWatch.stop();
    }

    @Override
    public void onCancel()
    {
        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", "The Analysis was cancelled ", NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    @Override
    public void onFinished()
    {
        super.onFinished();
        _analysisQueue = null;
        _stopWatch = null;
        _resultProvider = null;
    }

    @Override
    public void onSuccess()
    {
        if (_tableModel == null)
            return;

        _tableModel.ClearErrors();

        // TODO: Remove quick and dirty code and create subscription to Error provider
        //TODO: Change CogniCryptError model


        for (Set<CogniCryptError> errorSet : _resultProvider.GetErrors().values())
        {
            for (CogniCryptError error : errorSet)
            {
                _tableModel.AddError(error);
            }
        }

        NotificationProvider.ShowInfo(String.format("Analyzed %s APKs in %s", _analysedFilesCount, _stopWatch));
    }

    @Override
    public void onThrowable(@NotNull Throwable error)
    {
        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", "The analysis produced an unhandled exception. " +
                "The operation will terminate now.", NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }

    private boolean isIgnoredErrorType(AbstractError abstractError) {
        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();
        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY && !isSourceCodeClass(affectedClass)){
            return true;
        }
        //TODO Add options in preference page to enable / disable error types
        return abstractError instanceof IncompleteOperationError;
    }

    private boolean isSourceCodeClass(SootClass affectedClass) {

        String className = affectedClass.getName();

        // For nested private classes we need to find the root container, which is always the first entry of any possible '$' delimiter.
        String containerClass = className.split("\\$")[0];
        return sourceCodeJavaFiles.contains(containerClass);
    }
}
