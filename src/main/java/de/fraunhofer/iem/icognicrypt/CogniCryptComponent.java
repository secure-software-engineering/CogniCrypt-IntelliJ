package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IOutputFinderCache;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IProjectOutputFinder;

class CogniCryptComponent implements ProjectComponent, ICogniCryptComponent
{
    private Project _project;
    private IProjectOutputFinder _outputFinder;

    public CogniCryptComponent(Project project, IProjectOutputFinder outputFinder)
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
}

