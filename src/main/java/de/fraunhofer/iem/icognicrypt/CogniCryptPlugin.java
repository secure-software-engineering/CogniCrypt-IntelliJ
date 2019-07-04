package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectListener;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectManager;
import de.fraunhofer.iem.icognicrypt.analysis.CogniCryptAnalysisManager;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptToolWindowManager;

/**
 * This class gets initialized as soon as one project is loaded to the IDE.
 * It shall initialize global project-independent services.
 *
 * The attempt to create a second instance will cause an {@link CogniCryptException} to be thrown.
 *
 */
public class CogniCryptPlugin
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
        Initialize();
    }

    public void Initialize() throws CogniCryptException
    {
        if (_initialized)
            throw new CogniCryptException("CogniCrypt already initialized");

        CogniCryptProjectManager projectManager = ServiceManager.getService(CogniCryptProjectManager.class);
        CogniCryptProjectListener toolWindowManager = ServiceManager.getService(CogniCryptToolWindowManager.class);
        CogniCryptProjectListener analysisManager = ServiceManager.getService(CogniCryptAnalysisManager.class);

        projectManager.Subscribe(toolWindowManager);
        projectManager.Subscribe(analysisManager);

        _initialized = true;
    }
}
