package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;

interface IOutputFinderInternal
{
    Iterable<File> GetOutputFiles(Path projectPath, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException;

    Iterable<File> GetOutputFiles(Project project) throws OperationNotSupportedException, IOException, CogniCryptException;

    Iterable<File> GetOutputFiles(Project project, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException;
}
