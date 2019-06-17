package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import com.android.ide.common.build.ApkData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.ApkDataDeserializer;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;
import org.omg.CORBA.ARG_OUT;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class JavaModule
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
        _debugJson = Deserialize(Paths.get(_path.toString(), RelativeBuildPath, "debug\\output.json").toString());
        _releaseJson = Deserialize(Paths.get(_path.toString(), RelativeBuildPath, "release\\output.json").toString());
    }

    public String GetDebugOutputPath()
    {
        return GetOutputFilePath(_debugJson, false);
    }

    public String GetDebugOutputPathAbsolute()
    {
        return GetOutputFilePath(_debugJson, true);
    }

    public String GetReleaseOutputPath()
    {
        return GetOutputFilePath(_releaseJson, false);
    }

    public String GetReleaseOutputPathAbsolute()
    {
        return GetOutputFilePath(_releaseJson, true);
    }

    private String GetOutputFilePath(OutputJson outputJson, boolean absolute)
    {
        if (outputJson == null) return null;
        String outputFileName = outputJson.getApkData().getOutputFileName();
        if (!absolute) return outputFileName;
        return Paths.get(Paths.get(outputJson.GetFilePath()).getParent().toString(), outputFileName).toString();
    }

    private OutputJson Deserialize(String path)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ApkData.class, new ApkDataDeserializer());
        mapper.registerModule(module);
        try
        {
            OutputJson[] outputs = mapper.readValue(new File(path), OutputJson[].class);
            if (outputs.length == 1) {
                OutputJson output = outputs[0];
                output.SetFilePath(path);
                return outputs[0];
            }
            // TODO: I'm not sure if the Json-Array is ever filled with more than one entry. If so we need to change code here.
            throw new NotImplementedException();
        }
        catch (IOException e)
        {
            return null;
        }
    }
}

