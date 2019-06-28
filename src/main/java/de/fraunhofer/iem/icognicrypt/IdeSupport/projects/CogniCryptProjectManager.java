package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.core.Collections.*;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.inject.Inject;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;

public class CogniCryptProjectManager
{
    private final HashSet<Project> _projects = new HashSet<>();
    private Collection<WeakReference<ICogniCryptProjectListener>> _subscribers = new HashSet<>();

    @Inject
    private ICogniCryptProjectListener _test;

    public CogniCryptProjectManager(){
        System.out.println(_test);
    }

    @Immutable
    public IReadOnlyCollection<Project> GetOpenProject()
    {
        return new ReadOnlyCollection<>(_projects);
    }

    public void Subscribe(ICogniCryptProjectListener subscriber)
    {

    }

    public void UnSubscribe(ICogniCryptProjectListener subscriber)
    {

    }
}

