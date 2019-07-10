package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.extensions.ExtensionPointName;

public interface IResultsProviderListener
{
    ExtensionPointName<IResultsProviderListener> EP_NAME = ExtensionPointName.create("de.fraunhofer.iem.icognicrypt.resultListener");

    void OnResultAdded();

    void OnResultRemoved();

    void OnResultsCleared();
}
