package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.LineMarkersPass;
import com.intellij.ide.plugins.PluginManagerMain;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import crypto.analysis.errors.AbstractError;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ErrorProvider;
import org.jetbrains.annotations.NotNull;
import soot.G;
import soot.SootClass;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class AndroidProjectAnalysisQueue extends Task.Backgroundable{

    private static final Logger logger = Logger.getInstance(AndroidProjectAnalysisQueue.class);
    private Queue<CogniCryptAndroidAnalysis> analysisQueue;

    public AndroidProjectAnalysisQueue(Project p, Queue<CogniCryptAndroidAnalysis> analysisQueue){
        super(p, "Performing CogniCrypt Analysis");
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
            G.v().reset();
            CogniCryptAndroidAnalysis curr = analysisQueue.poll();

            try {
                Collection<AbstractError> results = curr.run();
                for(AbstractError abstractError : results){
                    //Add error to error provider
                    if (abstractError.getErrorLocation().getUnit().isPresent()) {
                        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();
                        ErrorProvider.addError(affectedClass.getName(), abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber() - 1, new CogniCryptError(abstractError.toErrorMarkerString(), affectedClass.getName()));
                    }
                }
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
    }
}
