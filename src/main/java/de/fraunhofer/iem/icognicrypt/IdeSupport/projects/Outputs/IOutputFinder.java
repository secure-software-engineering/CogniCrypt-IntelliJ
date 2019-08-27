package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.android.tools.layoutlib.annotations.NotNull;

import java.io.File;
import java.util.EnumSet;

public interface IOutputFinder
{
    @NotNull Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options);
}
