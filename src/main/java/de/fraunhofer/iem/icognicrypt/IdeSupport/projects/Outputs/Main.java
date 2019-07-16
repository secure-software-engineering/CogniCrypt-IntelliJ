package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.EnumSet;

public class Main
{
    public static void main(String[] args) throws IOException, OperationNotSupportedException, CogniCryptException
    {
        String tmpPath = "C:\\Users\\lrs\\AndroidStudioProjects\\HelloWorld";
        IOutputFinder outputFinder = AndroidStudioOutputFinder.GetInstance();


        EnumSet<OutputFinderOptions.Flags> statusFlags = EnumSet.noneOf(OutputFinderOptions.Flags.class);

        Iterable<File> files = outputFinder.GetOutputFiles(Paths.get(tmpPath), statusFlags);
        System.out.println(files);
    }
}
