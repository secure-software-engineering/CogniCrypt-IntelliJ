package de.fraunhofer.iem.icognicrypt.results;

public interface IResultProvider
{
    void GetResults();

    void AddResult();

    void RemoveResult();

    void RemoveAllResults();

    void Subscribe(IResultsProviderListener listener);

    void Unsubscribe(IResultsProviderListener listener);
}
