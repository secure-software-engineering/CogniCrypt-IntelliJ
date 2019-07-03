package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.results.ui.CogniCryptResultWindow;
import de.fraunhofer.iem.icognicrypt.results.ErrorProvider;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

public class AndroidProjectAnalysisQueue extends Task.Backgroundable{

    private static final Logger logger = Logger.getInstance(AndroidProjectAnalysis.class);
    private final CogniCryptToolWindowManager _toolWindowManager;
    private Queue<AndroidProjectAnalysis> _analysisQueue;

    private Project _project;

    public AndroidProjectAnalysisQueue(Project p, Queue<AndroidProjectAnalysis> analysisQueue){
        super(p, "Performing CogniCrypt Analysis");
        _project = p;
        _analysisQueue = analysisQueue;

        _toolWindowManager = ServiceManager.getService(CogniCryptToolWindowManager.class);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        // Remove errors before rerunning Cognicrypt
        ErrorProvider.clearError();

        int size = _analysisQueue.size();
        int index = 0;  Stopwatch w = Stopwatch.createStarted();
        while(!_analysisQueue.isEmpty()){
            index++;
            indicator.setText(String.format("Performing CogniCrypt Analysis (APK %s of %s)", index,size));
            AndroidProjectAnalysis curr = _analysisQueue.poll();
            try {
                curr.run();
            } catch (Throwable e){
                Notification notification = new Notification("CogniCrypt", "CogniCrypt", String.format("Crashed on %s", curr), NotificationType.INFORMATION);
                logger.error(e);
                Notifications.Bus.notify(notification);
            }
            indicator.setFraction((index / (double)size));
        }
        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", String.format("Analyzed %s APKs in %s", index,w), NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
        Notification errorNotification = new Notification("CogniCrypt", "CogniCrypt Info", String.format("Found %s errors in classes: ", ErrorProvider.getErrorCount()) + Joiner.on("\n").join(ErrorProvider.getErrorClasses()), NotificationType.INFORMATION);
        Notifications.Bus.notify(errorNotification);

        try
        {
            ToolWindow t  = _toolWindowManager.GetToolWindow(_project);
            CogniCryptResultWindow errorWindow =  _toolWindowManager.GetWindowModel(t, CogniCryptToolWindowManager.ResultsView, CogniCryptResultWindow.class);
            errorWindow.SetSearchText("Test Text");
        }
        catch (CogniCryptException ex)
        {
            ex.printStackTrace();
        }
    }
}
