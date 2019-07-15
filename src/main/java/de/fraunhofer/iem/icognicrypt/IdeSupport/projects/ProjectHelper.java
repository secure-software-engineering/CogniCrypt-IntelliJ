package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import java.awt.*;

public class ProjectHelper
{
    public Project GetActiveProject()
    {
        for (Project project : ProjectManager.getInstance().getOpenProjects())
        {
            Window window = WindowManager.getInstance().suggestParentWindow(project);
            if (window != null && window.isActive())
            {
                return project;
            }
        }
        return null;
    }

    public static Project GetProjectFromComponent(Component component)
    {
        DataContext d = DataManager.getInstance().getDataContext(component);
        return d.getData(PlatformDataKeys.PROJECT);
    }
}
