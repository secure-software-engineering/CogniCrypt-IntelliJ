package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.io.File;
import java.util.EnumSet;

public interface IOutputFinderCache
{
    void Invalidate();

    void Invalidate(EnumSet<OutputFinderOptions.Flags> options);

    Iterable<File> GetOutputFiles();

    Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options);
}
