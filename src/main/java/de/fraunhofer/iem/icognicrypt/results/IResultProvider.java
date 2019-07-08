package de.fraunhofer.iem.icognicrypt.results;

public interface IResultProvider
{
    void GetResults();

    void AddResult();

    void RemoveResult();

    void RemoveAllResults();

    void Subscribe(IErrorProviderListener listener);

    void Unsubscribe(IErrorProviderListener listener);
}
