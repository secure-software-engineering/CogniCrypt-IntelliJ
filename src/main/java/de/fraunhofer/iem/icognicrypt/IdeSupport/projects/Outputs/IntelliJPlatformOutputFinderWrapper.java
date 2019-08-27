package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IIdePlatformProvider;
import de.fraunhofer.iem.icognicrypt.core.Collections.Linq;
import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.Collections;
import java.util.EnumSet;

class IntelliJPlatformOutputFinderWrapper implements IProjectOutputFinder, Disposable
{
    private IPersistableCogniCryptSettings _settings;
    private IOutputFinderInternal _serviceProvider;
    private IOutputFinderCache _cache;
    private Project _project;


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
        _project = project;
        _settings = settings;
    }

    @Override
    @NotNull
    public Iterable<File> GetOutputFiles()
    {
        return GetOutputFiles(_settings.GetFindOutputOptions());
    }

    @Override
    @NotNull
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options)
    {
        return _cache.GetOutputFiles(options);
    }

    @Override
    public Iterable<File> GetOutputFilesFromDialog()
    {
        Iterable<String> cachedFiles = _cache.GetCachedDialogOutputs();
        if (Linq.any(cachedFiles) && Linq.all(cachedFiles, file -> new File(file).exists()))
        {
            String message = GenerateDialogCachedMessage(cachedFiles);

            MessageBox.MessageBoxResult result = MessageBox.Show(null, message, MessageBox.MessageBoxButton.YesNo, MessageBox.MessageBoxType.Question, MessageBox.MessageBoxResult.YesOK);
            if (result == MessageBox.MessageBoxResult.YesOK)
                return Linq.select(cachedFiles, file -> new File(file));
            else if (result == MessageBox.MessageBoxResult.Cancel)
                return Collections.EMPTY_LIST;
        }
        Iterable<File> selectedFiles =  _serviceProvider.GetOutputFilesFromDialog(_project);
        // TODO: decide to enable or disable [effect: no selected files from the dialog will invalidate cache too]
        //  _cache.InvalidateDialogOutput();
        _cache.SetDialogFiles(selectedFiles);
        return selectedFiles;
    }

    private String GenerateDialogCachedMessage(Iterable<String> files)
    {
        if (files == null)
            return "";
        String message = "Do you want to analyze the last files again:\r\n\r\n";
        for (String file : files)
            message += FilenameUtils.getName(file) + "\r\n";
        StringTrimming.TrimEnd(message, "\r\n");
        return message;
    }

    public IOutputFinderCache GetCache(){
        return _cache;
    }

    @Override
    public void dispose()
    {
        _serviceProvider = null;
        _settings = null;
        _project = null;
        _cache = null;
    }
}
