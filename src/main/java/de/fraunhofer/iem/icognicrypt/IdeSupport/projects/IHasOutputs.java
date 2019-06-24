package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

public interface IHasOutputs
{
    public void InvalidateOutput();

    public Iterable<String> GetOutputs(OutputFinderOptions options);

    public String GetDebugOutputPath();

    public String GetDebugOutputPathAbsolute();

    public String GetReleaseOutputPath();

    public String GetReleaseOutputPathAbsolute();
}
