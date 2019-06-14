package de.fraunhofer.iem.icognicrypt.projects;

import java.io.File;
import java.nio.file.Path;

public class AndroidStudioOutputFinder implements IOutputFinder
{
    private static IOutputFinder _instance;

    public static IOutputFinder GetInstance(){
        if (_instance == null)
            _instance = new AndroidStudioOutputFinder();
        return _instance;
    }


    public Iterable<File> GetOutputFiles(){
        return null;
    }

    public Iterable<File> GetOutputFiles(Path projectRootPath)
    {
        return null;
    }

    private AndroidStudioOutputFinder(){

    }
}

