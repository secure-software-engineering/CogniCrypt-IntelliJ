package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectType;
import com.intellij.openapi.wm.WindowManager;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.ObjectUtils;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectHelper
{
    public static Path GetProjectBasePath(Project project)
    {
        if (project == null)
            throw new NullArgumentException("project");
        return Paths.get(project.getBasePath());
    }

    public static Project GetActiveProject()
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
