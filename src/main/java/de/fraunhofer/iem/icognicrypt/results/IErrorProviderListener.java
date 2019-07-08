package de.fraunhofer.iem.icognicrypt.results;

public interface IErrorProviderListener
{
    void OnResultAdded();

    void OnResultRemoved();

    void OnResultsCleared();
}
