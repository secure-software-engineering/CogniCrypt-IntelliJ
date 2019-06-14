package de.fraunhofer.iem.icognicrypt.core.FileSystem;

import de.fraunhofer.iem.icognicrypt.core.Helpers.CastHelpers;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class FileSystemWatcher
{
    protected final int ModifiedWaitThreshold = 50;

    private boolean _isDisposed;
    private boolean _listenForFileChanges;
    private WatchThread _thread;
    private Map<java.nio.file.Path, Long> _timeStamps;

    protected Path Path;

    public Path GetPath()
    {
        return Path;
    }

    //TODO: Recursive is not supported yet
    public FileSystemWatcher(Path path, boolean recursive) throws IOException
    {
        Path = path;
        _timeStamps = new HashMap<>();
        AddFilesTimeStamps(path);
    }

    public final void Listen() throws OperationNotSupportedException, IOException
    {
        ThrowIfDisposed();
        synchronized (this)
        {
            if (_listenForFileChanges) return;
            _listenForFileChanges = true;
        }
        _thread = new WatchThread(this);
        _thread.run();
    }

    public final void Stop()
    {
        synchronized (this)
        {
            _listenForFileChanges = false;
        }
    }

    public final void Dispose() throws IOException
    {
        Stop();
        _thread.Dispose();
        _thread = null;
        Path = null;
        _isDisposed = true;
    }

    protected void HandleFileSystemOverflow(FileSystemEventsArgs e)
    {
        e.SetHandled(true);
        Object sender = e.GetSender();
        WatchThread watchThread = CastHelpers.SafeCast(sender, WatchThread.class);
        if (watchThread == null)
            return;
        watchThread.yield();
        OnOverflown();
    }

    protected void HandleFileSystemCreated(FileSystemEventsArgs e)
    {
        if (!IsListeningForTarget(e.GetTarget()))
            return;
        OnCreated();
        AddFileTimeStamps(e.GetTarget().toFile());
    }

    protected void HandleFileSystemDeleted(FileSystemEventsArgs e)
    {
        if (!IsListeningForTarget(e.GetTarget()))
            return;
        OnDeleted();
        RemoveFileFromTimeStamps(e.GetTarget().toFile());
    }

    protected void HandleFileSystemModified(FileSystemEventsArgs e)
    {
        if (IsListeningForTarget(e.GetTarget()))
        {
            Path path = e.GetTarget();
            Long oldFileModifiedTimeStamp = _timeStamps.get(path);
            Long newFileModifiedTimeStamp = path.toFile().lastModified();

            //Ignore file changes in between 50ms
            if (newFileModifiedTimeStamp > oldFileModifiedTimeStamp + ModifiedWaitThreshold /* || oldFileModifiedTimeStamp == null*/)
            {
                _timeStamps.remove(path);
                _timeStamps.put(path, path.toFile().lastModified());
                OnModified();
            }
        }
    }

    protected void OnModified()
    {
    }

    protected void OnDeleted()
    {
    }

    protected void OnCreated()
    {
    }

    protected void OnOverflown()
    {
    }

    protected boolean IsListeningForTarget(Path name)
    {
        return true;
    }

    protected void ThrowIfDisposed() throws OperationNotSupportedException
    {
        if (_isDisposed) throw new OperationNotSupportedException("File Watcher is already disposed");
    }

    private void AddFilesTimeStamps(Path directory)
    {
        File[] files = directory.toFile().listFiles();
        if (files != null)
            for (File file : files)
                if (file.isFile())
                    AddFileTimeStamps(file);
    }

    private void AddFileTimeStamps(File file)
    {
        _timeStamps.put(file.toPath(), file.lastModified());
    }

    private void RemoveFileFromTimeStamps(File file)
    {
        _timeStamps.remove(file.toPath());
    }

    private class WatchThread extends Thread
    {
        private FileSystemWatcher _watcher;
        private Map<WatchKey, Path> _keys;

        private WatchService _watchService;
        private final WatchKey _watchKey;

        public WatchThread(FileSystemWatcher watcher) throws IOException
        {
            _watcher = watcher;
            _watchService = FileSystems.getDefault().newWatchService();
            _watchKey = watcher.Path.register(_watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.OVERFLOW, StandardWatchEventKinds.ENTRY_DELETE);
            _keys = new HashMap<>();
            Path path = _watcher.GetPath();
            _keys.put(_watchKey, path);
        }

        @Override
        public void run()
        {
            while (_watcher._listenForFileChanges)
            {
                WatchKey key;
                try
                {
                    key = _watchService.poll(25, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException e)
                {
                    return;
                }
                Path dir = _keys.get(key);
                if (key == null || dir == null)
                {
                    Thread.yield();
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents())
                {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked") WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    String name = ev.context().toString();
                    Path path = dir.resolve(name);

                    FileSystemEventsArgs args = new FileSystemEventsArgs(this, path, name);

                    if (kind == StandardWatchEventKinds.OVERFLOW)
                    {
                        _watcher.HandleFileSystemOverflow(args);
                    }
                    else if (kind == StandardWatchEventKinds.ENTRY_CREATE)
                    {
                        _watcher.HandleFileSystemCreated(args);
                    }
                    else if (kind == StandardWatchEventKinds.ENTRY_DELETE)
                    {
                        _watcher.HandleFileSystemDeleted(args);
                    }

                    else if (kind == StandardWatchEventKinds.ENTRY_MODIFY)
                    {
                        _watcher.HandleFileSystemModified(args);
                    }

                    if (args.GetHandled()) continue;

                    boolean valid = key.reset();
                    if (!valid)
                    {
                        _keys.remove(key);
                        if (_keys.isEmpty()) break;
                    }
                }
                Thread.yield();
            }
        }

        public void Dispose() throws IOException
        {
            _watchService.close();
            _watchService = null;
        }
    }
}
