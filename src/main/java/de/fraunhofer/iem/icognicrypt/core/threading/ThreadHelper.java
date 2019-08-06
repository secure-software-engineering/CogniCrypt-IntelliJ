package de.fraunhofer.iem.icognicrypt.core.threading;

import com.intellij.openapi.application.ApplicationManager;

import java.awt.*;

public class ThreadHelper
{
    public static void DeferOnUIThread(Runnable action){
        if (!ApplicationManager.getApplication().isDispatchThread()){
            EventQueue.invokeLater(action);
        }
        else
            action.run();
    }
}
