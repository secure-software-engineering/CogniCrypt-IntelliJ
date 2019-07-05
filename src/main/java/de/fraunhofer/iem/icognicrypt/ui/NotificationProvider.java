package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

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
        Notifications.Bus.notify(notification);
    }
}
