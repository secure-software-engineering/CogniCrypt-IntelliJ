package de.fraunhofer.iem.icognicrypt.results;

import com.google.common.collect.Table;

import java.util.Set;

public interface IResultProvider
{
    void GetResults();

    void AddResult(CogniCryptAnalysisResult result);

    void RemoveResult();

    void RemoveAllResults();

    void Subscribe(IResultsProviderListener listener);

    void Unsubscribe(IResultsProviderListener listener);



    //Only for integration:
    void AddResult(String fullyQualifiedClassName, int lineNumber, CogniCryptError error);

    Table<String, Integer, Set<CogniCryptError>> GetErrors();

    Set<CogniCryptError> FindErrors(String javaAbsolutFilePath, int lineNumber);

    void ClearErrors();
}
