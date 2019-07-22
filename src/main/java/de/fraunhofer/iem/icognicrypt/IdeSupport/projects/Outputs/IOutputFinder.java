package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

public interface IOutputFinder
{
    Iterable<File> GetOutputFiles() throws OperationNotSupportedException, IOException, CogniCryptException;

    Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options) throws OperationNotSupportedException, IOException, CogniCryptException;

    Iterable<File> GetOutputFiles(Project project) throws OperationNotSupportedException, IOException, CogniCryptException;

    Iterable<File> GetOutputFiles(Project project, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException;
}

