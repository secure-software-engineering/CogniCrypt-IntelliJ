package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptErrorWindow;
import org.jetbrains.annotations.NotNull;


public class CogniCryptToolWindowFactory implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        CogniCryptWindowBase errorWindow = new CogniCryptErrorWindow(toolWindow);
        CogniCryptWindowBase testWindow = new TestWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content errorWindowContent = contentFactory.createContent(errorWindow.GetContent(), "Errors", false);
        Content testWindowContent = contentFactory.createContent(testWindow.GetContent(), "TestPage", false);

        toolWindow.getContentManager().addContent(errorWindowContent);
        toolWindow.getContentManager().addContent(testWindowContent);
    }
}

