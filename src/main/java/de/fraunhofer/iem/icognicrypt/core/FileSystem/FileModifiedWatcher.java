package de.fraunhofer.iem.icognicrypt.core.FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


public class FileModifiedWatcher extends FileSystemWatcher
{
    protected HashSet<String> FileNames;

    public Iterable<String> GetFilesNames()
    {
        return new ArrayList<>(FileNames);
    }

    public FileModifiedWatcher(Path path) throws IOException
    {
        this(path, new ArrayList<>());
    }

    public FileModifiedWatcher(Path path, Collection<String> fileNames) throws IOException
    {
        super(path, false);
        FileNames = new HashSet<>(fileNames);
    }

    public FileModifiedWatcher(Path path, String... fileNames) throws IOException
    {
        this(path, new ArrayList<String>(Arrays.asList(fileNames)));
    }

    public FileModifiedWatcher(File file) throws IOException
    {
        this(Paths.get(file.getParent()), Arrays.asList(file.getName()));
    }

    public void AddFile(String fileName)
    {
        FileNames.add(fileName);
    }

    public void RemoveFile(String fileName)
    {
        FileNames.remove(fileName);
    }

    @Override
    protected final void OnDeleted(){
        super.OnDeleted();
    }

    @Override
    protected final void OnCreated(){
        super.OnCreated();
    }

    @Override
    protected final void OnOverflown(){
        super.OnOverflown();
    }

    @Override
    protected void OnModified()
    {
    }

    @Override
    protected boolean IsListeningForTarget(Path target)
    {
        if (!target.toFile().isFile())
            return false;
        if (FileNames.isEmpty()) return true;
        return FileNames.contains(target.toFile().getName());
    }
}
