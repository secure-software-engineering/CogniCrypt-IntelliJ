package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IIdePlatformProvider;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.EnumSet;

class IntelliJPlatformOutputFinderWrapper implements IProjectOutputFinder, Disposable
{
    private IOutputFinderInternal _serviceProvider;
    private IOutputFinderCache _cache;

    private IntelliJPlatformOutputFinderWrapper(Project project, IIdePlatformProvider platformProvider, IPersistableCogniCryptSettings settings) throws CogniCryptException
    {
        switch (platformProvider.GetRunningPlatform())
        {
            case Unknown:
                throw new CogniCryptException("Target IDE is not supported");
            case IntelliJ:
                throw new NotImplementedException();
            case AndroidStudio:
                _serviceProvider = new AndroidStudioOutputFinder();
        }
        _cache = new OutputFinderCache(project, _serviceProvider, settings);
    }

    @Override
    public Iterable<File> GetOutputFiles()
    {
        return _cache.GetOutputFiles();
    }

    @Override
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options)
    {
        return _cache.GetOutputFiles(options);
    }

    @Override
    public Iterable<File> GetOutputFilesFromDialog()
    {
        return _cache.GetOutputFilesFromDialog();
    }

    public IOutputFinderCache GetCache(){
        return _cache;
    }

    @Override
    public void dispose()
    {
        _serviceProvider = null;
        _cache = null;
    }
}
