package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import de.fraunhofer.iem.icognicrypt.actions.Actions;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ErrorWindowFactory implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        Component component = toolWindow.getComponent();
        Actions.TryExecute("ICognicrypt.ExecuteAnalysis", component);
    }
}
