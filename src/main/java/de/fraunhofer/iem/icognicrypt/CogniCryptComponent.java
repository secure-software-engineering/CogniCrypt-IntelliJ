package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IOutputFinder;

class CogniCryptComponent implements ProjectComponent, ICogniCryptComponent
{
    private Project _project;
    private IOutputFinder _outputFinder;

    public CogniCryptComponent(Project project, IOutputFinder outputFinder)
    {
        _project = project;
        _outputFinder = outputFinder;
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
        _outputFinder = null;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}

