package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main
{
    public static void main(String[] args) throws IOException, OperationNotSupportedException, CogniCryptException
    {
        String tmpPath = "C:\\Users\\lrs\\AndroidStudioProjects\\HelloWorld";
        IOutputFinder outputFinder = AndroidStudioOutputFinder.GetInstance();
        Iterable<File> files = outputFinder.GetOutputFiles(Paths.get(tmpPath), OutputFinderOptions.AnyBuildType);
        System.out.println(files);
    }
}
