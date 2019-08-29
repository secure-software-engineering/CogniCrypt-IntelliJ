package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

public interface IOutputFinderCache extends IOutputFinder
{
    void Invalidate();

    void Invalidate(Set<OutputFinderOptions.Flags> options);


    ReadOnlyCollection<String> GetCachedDialogOutputs();

    void InvalidateDialogOutput();

    void SetDialogFiles(Iterable<File> selectedFiles);


    ReadOnlyCollection<String> GetCachedMultipleFileSelection();

    void InvalidateMultipleSelectedFiles();

    void SetMultipleFileSelection(Iterable<File> filePaths);
}
