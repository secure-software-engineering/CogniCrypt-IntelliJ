package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.util.EnumSet;

public interface IHasOutputs
{
    void InvalidateOutput();

    Iterable<String> GetOutputs(EnumSet<OutputFinderOptions.Flags> options);

    String GetDebugOutputPath();

    String GetReleaseOutputPath();
}