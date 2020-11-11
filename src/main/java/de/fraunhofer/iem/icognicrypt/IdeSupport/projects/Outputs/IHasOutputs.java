package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.util.Set;

public interface IHasOutputs
{
    void InvalidateOutput();

    Iterable<String> GetOutputs(Set<OutputFinderOptions.Flags> options);

    String GetDebugOutputPath();

    String GetReleaseOutputPath();
}