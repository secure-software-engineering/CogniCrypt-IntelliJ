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

public class JavaModule implements IHasOutputManager
{
    // TODO: Apparently libraries are build in a folder called aar. We need to decide if we want to check for the too, or just support apk files.
    private static final String RelativeBuildPath = "build\\outputs\\apk\\";

    private Path _path;

    private OutputJson _debugJson;
    private OutputJson _releaseJson;

    private IHasOutputs _outputManager;

    public IHasOutputs GetOutputManager() { return _outputManager;}

    public JavaModule(String path) throws JavaModuleNotFoundException
    {
        Path realPath = Paths.get(path);
        if (!realPath.toFile().exists()) throw new JavaModuleNotFoundException();
        _path = realPath;

        _outputManager = new OutputManager(this);
        _outputManager.InvalidateOutput();

    }

    private class OutputManager extends OutputJsonManager
    {
        private final JavaModule _owner;

        public OutputManager(JavaModule owner){
            _owner = owner;
        }

        @Override
        public void InvalidateOutput()
        {
            DebugJson = OutputJson.Deserialize(Paths.get(_path.toString(), _owner.RelativeBuildPath, "debug\\output.json").toString());
            ReleaseJson = OutputJson.Deserialize(Paths.get(_path.toString(), _owner.RelativeBuildPath, "release\\output.json").toString());
        }
    }
}

