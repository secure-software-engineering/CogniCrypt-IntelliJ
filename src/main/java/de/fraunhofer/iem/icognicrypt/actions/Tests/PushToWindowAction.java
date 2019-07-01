package de.fraunhofer.iem.icognicrypt.actions.Tests;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.actions.CogniCryptAction;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptErrorWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class PushToWindowAction extends CogniCryptAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project p = e.getDataContext().getData(PlatformDataKeys.PROJECT);

        CogniCryptToolWindowManager toolWindowManager = ServiceManager.getService(CogniCryptToolWindowManager.class);

        try
        {
            ToolWindow t  = toolWindowManager.GetToolWindow(p);
            CogniCryptErrorWindow errorWindow =  toolWindowManager.GetWindowModel(t, CogniCryptToolWindowManager.ErrorView, CogniCryptErrorWindow.class);
            errorWindow.SetText("Test Text");
        }
        catch (CogniCryptException ex)
        {
            ex.printStackTrace();
        }
    }
}
