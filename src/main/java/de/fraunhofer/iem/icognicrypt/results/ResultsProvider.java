package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.util.containers.WeakList;

public class ResultsProvider implements IResultProvider
{
    WeakList<IResultsProviderListener> _listeners = new WeakList<>();

    @Override
    public void GetResults()
    {

    }

    @Override
    public void AddResult()
    {

    }

    @Override
    public void RemoveResult()
    {

    }

    @Override
    public void RemoveAllResults()
    {

    }

    @Override
    public void Subscribe(IResultsProviderListener listener)
    {
        if (_listeners.contains(listener))
            return;
        _listeners.add(listener);
    }

    @Override
    public void Unsubscribe(IResultsProviderListener listener)
    {
        _listeners.remove(listener);
    }
}
