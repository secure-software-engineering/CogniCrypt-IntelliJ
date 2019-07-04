package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;

public abstract class CogniCryptProjectListener implements ICogniCryptProjectListener, Disposable
{

    @Override
    public void dispose()
    {
    }

    @Override
    public void OnProjectOpened(Project project)
    {
    }

    @Override
    public void OnProjectInitialized(Project project)
    {
    }

    @Override
    public void OnProjectClosing(Project project)
    {
    }

    @Override
    public void OnProjectClosed(Project project)
    {
    }
}
