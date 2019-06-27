package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import org.jetbrains.annotations.NotNull;

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
