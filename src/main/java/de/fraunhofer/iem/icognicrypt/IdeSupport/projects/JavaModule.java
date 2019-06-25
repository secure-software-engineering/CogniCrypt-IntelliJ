package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.ApkDataDeserializer;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.IApkData;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;
import org.omg.CORBA.ARG_OUT;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;

public class JavaModule implements IHasOutputs
{
    // TODO: Apparently libraries are build in a folder called aar. We need to decide if we want to check for the too, or just support apk files.
    private static final String RelativeBuildPath = "build\\outputs\\apk\\";

    private Path _path;

    private OutputJson _debugJson;
    private OutputJson _releaseJson;

    public OutputJson GetDebug()
    {
        return _debugJson;
    }

    public OutputJson GetRelease()
    {
        return _releaseJson;
    }

    public OutputJson GetReleaseOutput()
    {
        return _releaseJson;
    }

    public JavaModule(String path) throws JavaModuleNotFoundException
    {
        Path realPath = Paths.get(path);
        if (!realPath.toFile().exists()) throw new JavaModuleNotFoundException();
        _path = realPath;

        InvalidateOutput();
    }

    public void InvalidateOutput()
    {
        //TODO: Invalidate after each build triggered to assure the jsons are up-to-date
        _debugJson = OutputJson.Deserialize(Paths.get(_path.toString(), RelativeBuildPath, "debug\\output.json").toString());
        _releaseJson = OutputJson.Deserialize(Paths.get(_path.toString(), RelativeBuildPath, "release\\output.json").toString());
    }

    public Iterable<String> GetOutputs(OutputFinderOptions options)
    {
        HashSet<String> result = new HashSet<>();

        if (_debugJson != null && (options == OutputFinderOptions.DebugOnly || options == OutputFinderOptions.AnyBuildType)){
            result.add(GetDebugOutputPath());
        }

        if (_releaseJson != null && (options == OutputFinderOptions.ReleaseOnly || options == OutputFinderOptions.AnyBuildType)){
            result.add(GetReleaseOutputPath());
        }
        return result;
    }

    public String GetDebugOutputPath()
    {
        return GetOutputFilePath(_debugJson, true);
    }

    public String GetDebugOutputPathRelative()
    {
        return GetOutputFilePath(_debugJson, false);
    }

    public String GetReleaseOutputPath()
    {
        return GetOutputFilePath(_releaseJson, true);
    }

    public String GetReleaseOutputPathRelative()
    {
        return GetOutputFilePath(_releaseJson, false);
    }

    private String GetOutputFilePath(OutputJson outputJson, boolean absolute)
    {
        if (outputJson == null)
            return null;
        return outputJson.GetOutputFilePath(absolute);
    }
}

