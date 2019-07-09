package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

class CogniCryptComponent implements ProjectComponent, ICogniCryptComponent
{
    private Project _project;

    public CogniCryptComponent(Project project){
        _project = project;
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
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}

