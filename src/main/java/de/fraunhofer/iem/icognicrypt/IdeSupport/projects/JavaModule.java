package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import com.android.ide.common.build.ApkData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.ItemDeserializer;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class JavaModule
{
    private Path _path;

    private OutputJson _debugJson;
    private OutputJson _releaseJson;

    private static final String RelativeBuildPath = "build\\outputs\\apk\\";

    public JavaModule(String path) throws JavaModuleNotFoundException, IOException
    {
        Path realPath = Paths.get(path);
        if (!realPath.toFile().exists()) throw new JavaModuleNotFoundException();
        _path = realPath;


        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ApkData.class, new ItemDeserializer());
        mapper.registerModule(module);
        OutputJson[] test = mapper.readValue(new File("C:\\Users\\lrs\\Desktop\\json 1.json"), OutputJson[].class);

    }

    public void InvalidateOutput()
    {

    }
}

