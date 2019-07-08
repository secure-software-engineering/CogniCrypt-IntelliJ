package de.fraunhofer.iem.icognicrypt.results;

public interface IResultsProviderListener
{
    void OnResultAdded();

    void OnResultRemoved();

    void OnResultsCleared();
}
