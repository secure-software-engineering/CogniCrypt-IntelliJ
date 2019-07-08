package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class TestFactory implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        TestServiceB c = ServiceManager.getService(TestServiceB.class);
        TestServiceB b = ServiceManager.getService(project, TestServiceB.class);
        TestServiceB a = ServiceManager.getService(project, TestServiceB.class);
    }
}
