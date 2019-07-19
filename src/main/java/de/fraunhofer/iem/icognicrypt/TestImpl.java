package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.results.IResultsProviderListener;

public class TestImpl implements IResultsProviderListener
{
    public TestImpl(Project p)
    {
        int i = 0;
        i++;
    }

    @Override
    public void OnResultAdded()
    {

    }

    @Override
    public void OnResultRemoved()
    {

    }

    @Override
    public void OnResultsCleared()
    {

    }
}
