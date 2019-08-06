package de.fraunhofer.iem.icognicrypt.ui.ToolWindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.results.ui.CogniCryptResultWindowFactory;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import org.jetbrains.annotations.NotNull;

public class CogniCryptToolWindowFactory implements ToolWindowFactory, DumbAware
{
    private static ContentFactory ContentFactory = com.intellij.ui.content.ContentFactory.SERVICE.getInstance();

    @Override
    public void init(ToolWindow window)
    {
        window.setStripeTitle("CogniCrypt");
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        ICogniCryptToolWindowManager toolWindowManager = project.getComponent(ICogniCryptToolWindowManager.class);
        toolWindowManager.RegisterToolWindow(toolWindow);

        // Error Window
        ICogniCryptWindowBase resultsWindow = CogniCryptResultWindowFactory.CreateWindow(toolWindow, project);
        Content resultsWindowContent = ContentFactory.createContent(resultsWindow.GetContent(), resultsWindow.GetDisplayName(), false);
        toolWindow.getContentManager().addContent(resultsWindowContent);

        //Other Windows below...

        try
        {
            toolWindowManager.RegisterModel(ToolWindowModelType.Results, resultsWindow);
        }
        catch (CogniCryptException e)
        {
            NotificationProvider.ShowError("Unable to register the plugin's tool window");
        }
    }
}
