package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    // TODO: If AS supports detecting when a project is load we can omit this method as we should always read paths directly from the IDE
    public Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException
    {
        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        //GradleSettings settings = new GradleSettings(projectPath);

        return null;
    }

    private AndroidStudioOutputFinder(){

    }
}

