package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

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

    private MessageBusConnection _connection;

    public static CogniCryptPlugin GetInstance() throws CogniCryptException
    {
        if (_instance == null)
            throw new CogniCryptException("CogniCrypt is not initialized.");
        return _instance;
    }

    private CogniCryptPlugin() throws CogniCryptException
    {
        if (_instance != null)
            throw new CogniCryptException("CogniCrypt already initialized");
        _instance = this;
        Initialize();
    }

    public void Initialize()
    {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        _connection = bus.connect();
//        _connection.subscribe(ProjectManager.TOPIC, new ProjectManagerListener()
//        {
//            @Override
//            public void projectOpened(@NotNull Project project)
//            {
//            }
//        });
    }
}
