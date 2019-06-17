package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.results.ErrorProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class AndroidProjectAnalysisQueue extends Task.Backgroundable{

    private Queue<AndroidProjectAnalysis> analysisQueue;
    private static final Logger logger = Logger.getInstance(AndroidProjectAnalysisQueue.class);

    public AndroidProjectAnalysisQueue(Project p, Queue<AndroidProjectAnalysis> analysisQueue){
        super(null, "Performing CogniCrypt Analysis");
        this.analysisQueue = analysisQueue;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        //Remove errors before rerunning Cognicrypt
        ErrorProvider.clearError();

        int size = analysisQueue.size();
        int index = 0;  Stopwatch w = Stopwatch.createStarted();
        while(!analysisQueue.isEmpty()){
            index++;
            indicator.setText(String.format("Performing CogniCrypt Analysis (APK %s of %s)", index,size));
            AndroidProjectAnalysis curr = analysisQueue.poll();
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
    }
}
