package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IIdePlatformProvider;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

public class IntelliJPlatformOutputFinderWrapper implements IOutputFinder, Disposable
{
    private IOutputFinder _serviceProvider;
    private Project _project;

    private IntelliJPlatformOutputFinderWrapper(Project project, IIdePlatformProvider platformProvider) throws CogniCryptException
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
        _project = project;
    }

    @Override
    public Iterable<File> GetOutputFiles() throws OperationNotSupportedException, IOException, CogniCryptException
    {
        return GetOutputFiles(_project);
    }

    @Override
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options) throws OperationNotSupportedException, IOException, CogniCryptException
    {
        return GetOutputFiles(_project, options);
    }

    @Override
    public Iterable<File> GetOutputFiles(Project project) throws OperationNotSupportedException, IOException, CogniCryptException
    {
        return _serviceProvider.GetOutputFiles(project);
    }

    @Override
    public Iterable<File> GetOutputFiles(Project project, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        return _serviceProvider.GetOutputFiles(project, options);
    }

    @Override
    public void dispose()
    {
        _serviceProvider = null;
    }
}
