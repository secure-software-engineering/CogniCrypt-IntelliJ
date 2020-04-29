package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.ide.DataManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import static de.fraunhofer.iem.icognicrypt.IdeSupport.projects.ProjectHelper.GetActiveProject;


public class NotificationProvider
{

    public static void Warn(String message)
    {
        ShowNotification(message, NotificationType.WARNING);
    }

    public static void ShowInfo(String message)
    {
        ShowNotification(message, NotificationType.INFORMATION);
    }

    public static void ShowError(String message)
    {
        ShowNotification(message, NotificationType.ERROR);
    }

    private static void ShowNotification(String message, NotificationType type)
    {
        Notification notification = new Notification("CogniCrypt", "CogniCrypt Message", message, type);
        Project project= GetActiveProject();
        DataContext d = DataManager.getInstance().getDataContext();
        Project project1= d.getData(PlatformDataKeys.PROJECT);
        if(project==project1) {
            Notifications.Bus.notify(notification, project);
        }

    }

}
