package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;

import java.util.HashSet;

public abstract class OutputManager implements IHasOutputs
{
    protected OutputJson DebugJson;
    protected OutputJson ReleaseJson;


    @Override
    public abstract void InvalidateOutput();

    @Override
    public Iterable<String> GetOutputs(OutputFinderOptions options)
    {
        HashSet<String> result = new HashSet<>();

        if ((options == OutputFinderOptions.DebugOnly || options == OutputFinderOptions.AnyBuildType) && DebugJson != null)
        {
            result.add(GetDebugOutputPath());
        }

        if ((options == OutputFinderOptions.ReleaseOnly || options == OutputFinderOptions.AnyBuildType) && ReleaseJson != null)
        {
            result.add(GetReleaseOutputPath());
        }
        return result;
    }

    @Override
    public String GetDebugOutputPath()
    {
        return GetOutputFilePath(DebugJson, true);
    }

    @Override
    public String GetReleaseOutputPath()
    {
        return GetOutputFilePath(ReleaseJson, true);
    }

    private String GetOutputFilePath(OutputJson outputJson, boolean absolute)
    {
        if (outputJson == null)
            return null;
        return outputJson.GetOutputFilePath(absolute);
    }
}
