package de.fraunhofer.iem.icognicrypt.analysis;

import com.android.tools.profiler.proto.IoProfiler;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;

// TODO: I'm not sure yet if this class will ever do something. Remove if it reveals no purpose
// Maybe here we can track previous analysis results and other relevant information.
// Based on the Settings we can enable/disable compilation listening and other stuff.
final class CogniCryptAnalysisManager implements ProjectComponent, ICogniCryptAnalysisManager
{
    IResultProvider _resultsProvider;

    public IResultProvider GetResultProvider(){
        return _resultsProvider;
    }

    CogniCryptAnalysisManager(Project project)
    {
        _resultsProvider = ServiceManager.getService(project, IResultProvider.class);
    }

    @Override
    public void disposeComponent()
    {
        _resultsProvider = null;
    }
}

