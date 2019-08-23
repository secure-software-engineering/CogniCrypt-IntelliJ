package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.IdeSupport.build.IIntelliJPlatformBuildListener;
import de.fraunhofer.iem.icognicrypt.core.Collections.Linq;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;

import java.io.File;
import java.util.*;

class OutputFinderCache implements Disposable, IOutputFinderCache
{
    private IPersistableCogniCryptSettings _settings;
    private IOutputFinderInternal _serviceProvider;
    private MessageBusConnection _connection;
    private Project _project;

    private final Hashtable<Integer, Iterable<File>> _cache = new Hashtable<>();

    private HashSet<String> _cachedDialogFiles = new HashSet<>();

    OutputFinderCache(Project project, IOutputFinderInternal serviceProvider, IPersistableCogniCryptSettings settings)
    {
        _connection = project.getMessageBus().connect();
        _project = project;
        _connection.subscribe(IIntelliJPlatformBuildListener.TOPIC, new IIntelliJPlatformBuildListener(){
            @Override
            public void buildFinished(Project project)
            {
                Invalidate();
            }
        });
        _serviceProvider = serviceProvider;
         _settings = settings;

         Invalidate(_settings.GetFindOutputOptions());
    }

    @Override
    public Iterable<File> GetOutputFiles()
    {
        return GetOutputFiles(_settings.GetFindOutputOptions());
    }

    @Override
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options)
    {
        int settingsValue = OutputFinderOptions.getStatusValue(options);

        if (settingsValue == 0)
            return GetOutputFilesFromDialog();

        if (!_cache.containsKey(settingsValue))
            Invalidate(options);

        Iterable<File> files = _cache.get(settingsValue);
        if (Linq.all(files, file -> file.exists())){
            return files;
        }
        Invalidate(options);
        return _cache.get(settingsValue);
    }

    @Override
    public void Invalidate()
    {
        for (Integer optionsValue: _cache.keySet())
        {
            EnumSet<OutputFinderOptions.Flags> options = OutputFinderOptions.getStatusFlags(optionsValue);
            Invalidate(options);
        }
    }

    @Override
    public void Invalidate(EnumSet<OutputFinderOptions.Flags> options)
    {
        if (!Linq.any(options))
            return;

        Collection<File> files = new ArrayList<>();
        try
        {
            for (File file: _serviceProvider.GetOutputFiles(_project,options))
               files.add(file);
        }
        catch (Exception e)
        {
        }
        finally
        {
            int key = OutputFinderOptions.getStatusValue(options);
            _cache.put(key, files);
        }
    }

    @Override
    public Iterable<File> GetOutputFilesFromDialog()
    {
        ReadOnlyCollection<String> cachedFiles = GetCachedDialogOutputs();



        return _serviceProvider.GetOutputFilesFromDialog(_project);
    }

    public ReadOnlyCollection<String> GetCachedDialogOutputs()
    {
        return new ReadOnlyCollection<>(_cachedDialogFiles);
    }

    @Override
    public void dispose()
    {
        _connection.disconnect();
        _connection = null;
        _serviceProvider = null;
        _project = null;
        _settings = null;
    }
}

