package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;

public final class CogniCryptProjectManager implements ProjectManagerListener
{
    private static CogniCryptProjectManager _instance;

    private final HashSet<Project> _projects = new HashSet<>();
    private Collection<WeakReference<ICogniCryptProjectListener>> _subscribers = new HashSet<>();

    private CogniCryptProjectManager()
    {
        CogniCryptProjectManager.GetInstance();
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(ProjectManager.TOPIC, this);
    }

    public static CogniCryptProjectManager GetInstance()
    {
        if (_instance == null) _instance = new CogniCryptProjectManager();
        return _instance;
    }

    public IReadOnlyCollection<Project> GetOpenProject()
    {
        return new ReadOnlyCollection<>(_projects);
    }

    public void Subscribe(ICogniCryptProjectListener subscriber)
    {
        _subscribers.add(new WeakReference<>(subscriber));
    }

    public void UnSubscribe(ICogniCryptProjectListener subscriber)
    {
        _subscribers.remove(subscriber);
    }
}

