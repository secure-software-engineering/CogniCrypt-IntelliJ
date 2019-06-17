package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import java.nio.file.*;

public class JavaModule
{
    private Path _path;

    private OutputJsonFile _debugJson;
    private OutputJsonFile _releaseJson;

    private static final String RelativeBuildPath = "build\\outputs\\apk\\";

    public JavaModule(String path) throws JavaModuleNotFoundException
    {
        Path realPath = Paths.get(path);
        if (!realPath.toFile().exists())
            throw new JavaModuleNotFoundException();
        _path = realPath;
    }

    public void InvalidateOutput()
    {

    }
}

class OutputJsonFile
{

}

