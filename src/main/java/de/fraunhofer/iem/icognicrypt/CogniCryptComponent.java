package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IOutputFinderCache;

class CogniCryptComponent implements ProjectComponent, ICogniCryptComponent
{
    private IOutputFinderCache _outputFinderCache;
    private Project _project;

    public CogniCryptComponent(Project project, IOutputFinderCache outputFinderCache)
    {
        _project = project;
        _outputFinderCache = outputFinderCache;
    }

    @Override
    public void projectOpened()
    {
    }

    @Override
    public void projectClosed()
    {
    }

    @Override
    public void disposeComponent()
    {
        _project = null;
        _outputFinderCache = null;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}

