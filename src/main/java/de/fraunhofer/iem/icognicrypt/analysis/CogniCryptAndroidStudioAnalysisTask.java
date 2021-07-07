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
import com.intellij.webcore.packaging.ManageRepoDialog;
import crypto.analysis.errors.AbstractError;
import crypto.analysis.errors.IncompleteOperationError;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.actions.RunCogniCryptAction;
import de.fraunhofer.iem.icognicrypt.core.Language.JvmClassNameUtils;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import org.jetbrains.annotations.NotNull;
import soot.G;
import soot.SootClass;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class CogniCryptAndroidStudioAnalysisTask extends Task.Backgroundable{

    private static final Logger logger = Logger.getInstance(CogniCryptAndroidStudioAnalysisTask.class);
    private Stopwatch _stopWatch;
    private Queue<CogniCryptAndroidAnalysis> _analysisQueue;

    private int _analysedFilesCount;

    private final List<String> sourceCodeJavaFiles;

    private IResultProvider _resultProvider;

    public CogniCryptAndroidStudioAnalysisTask(Project p, Queue<CogniCryptAndroidAnalysis> analysisQueue){
        super(p, "Performing CogniCrypt Analysis");
        _analysisQueue = analysisQueue;

        _resultProvider = ServiceManager.getService(p, IResultProvider.class);

        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY) {
            sourceCodeJavaFiles = JvmClassNameUtils.findFullyQualifiedClassNames(getProject());
        } else {
            sourceCodeJavaFiles = Lists.newArrayList();
        }

        _stopWatch = Stopwatch.createUnstarted();
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {

        // Remove errors before rerunning Cognicrypt
        _resultProvider.RemoveAllResults();

        int size = _analysisQueue.size();

        _stopWatch.start();
        while(!_analysisQueue.isEmpty())
        {
            indicator.checkCanceled();

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

                        String name = abstractError.getErrorLocation().getMethod().getDeclaringClass().getName();
                        int line = abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber();

                        //_resultProvider.AddResult(new CogniCryptAnalysisResult(abstractError));
                        _resultProvider.AddResult(name, line, new CogniCryptError(abstractError.toErrorMarkerString(), name, line));
                    }
                }
            } catch (Throwable e){
                Notification notification = new Notification("CogniCrypt", "CogniCrypt", String.format("Crashed on %s", curr), NotificationType.ERROR);
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
        NotificationProvider.ShowInfo("The Analysis was cancelled");
    }

    @Override
    public void onFinished()
    {
        super.onFinished();
        _analysisQueue = null;
        _stopWatch = null;
        _resultProvider = null;
        RunCogniCryptAction.SetFlag(true);
    }

    @Override
    public void onSuccess()
    {
        NotificationProvider.ShowInfo(String.format("Analyzed %s APKs in %s", _analysedFilesCount, _stopWatch));
    }

    @Override
    public void onThrowable(@NotNull Throwable error)
    {
        NotificationProvider.ShowError("The analysis produced an unhandled exception. " +
                "The operation will terminate now.");
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
