package de.fraunhofer.iem.icognicrypt.analysis;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IReparseableElementType;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectListener;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;
import de.fraunhofer.iem.icognicrypt.results.ResultsProvider;
import kotlin.Lazy;

import java.util.WeakHashMap;

//TODO: I'm not sure yet if this class will ever do something. Remove if it reveals no purpose
public final class CogniCryptAnalysisManager extends CogniCryptProjectListener
{
    private WeakHashMap<Project, Lazy<IResultProvider>> _projectResultsProviderMapping = new WeakHashMap<>();

    public IResultProvider GetResultProvider(Project project){
        if (_projectResultsProviderMapping.containsKey(project))
            return _projectResultsProviderMapping.get(project).getValue();
        return null;
    }

    @Override
    public void OnProjectInitialized(Project project)
    {
        super.OnProjectInitialized(project);

        Lazy<IResultProvider> lazy = new Lazy<IResultProvider>()
        {
            IResultProvider _value = null;

            @Override
            public IResultProvider getValue()
            {
                if (_value == null)
                    _value = new ResultsProvider();
                return _value;
            }

            @Override
            public boolean isInitialized()
            {
                return _value != null;
            }
        };

        _projectResultsProviderMapping.put(project, lazy);
    }

    @Override
    public void OnProjectClosed(Project project)
    {
        super.OnProjectClosed(project);
        _projectResultsProviderMapping.remove(project);
    }
}
