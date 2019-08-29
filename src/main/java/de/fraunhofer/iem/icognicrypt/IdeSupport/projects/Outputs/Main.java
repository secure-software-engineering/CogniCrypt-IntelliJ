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
        IOutputFinderInternal outputFinder = new AndroidStudioOutputFinderInternal();

        //EnumSet<OutputFinderOptions.Flags> statusFlags = EnumSet.noneOf(OutputFinderOptions.Flags.class);
        EnumSet<OutputFinderOptions.Flags> statusFlags = EnumSet.of(OutputFinderOptions.Flags.AnyBuild, OutputFinderOptions.Flags.SignedOnly);

        Iterable<File> files = outputFinder.GetOutputFiles(Paths.get(tmpPath), statusFlags);
        System.out.println(files);
    }
}
