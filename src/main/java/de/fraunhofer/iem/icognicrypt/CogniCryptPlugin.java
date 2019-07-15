package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.progress.ProgressIndicator;
import de.fraunhofer.iem.icognicrypt.core.BackgroundComponent;
import com.intellij.openapi.components.ServiceManager;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectListener;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectManager;
import de.fraunhofer.iem.icognicrypt.analysis.CogniCryptAnalysisManager;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

public class CogniCryptPlugin extends BackgroundComponent
{
    private static CogniCryptPlugin _instance;

    private boolean _initialized;

    public static CogniCryptPlugin GetInstance() throws CogniCryptException
    {
        if (_instance == null)
            throw new CogniCryptException("CogniCrypt is not initialized.");
        return _instance;
    }

    private CogniCryptPlugin() throws CogniCryptException
    {
        if (_instance != null)
            throw new CogniCryptException("CogniCrypt already instanciated");
        _instance = this;
    }

    @Override
    protected void InitializeInBackground(ProgressIndicator indicator) throws CogniCryptException
    {
        if (_initialized)
            throw new CogniCryptException("CogniCrypt already initialized");
        _initialized = true;

        // TODO: If we have global initializations to make, do them here
    }
}
