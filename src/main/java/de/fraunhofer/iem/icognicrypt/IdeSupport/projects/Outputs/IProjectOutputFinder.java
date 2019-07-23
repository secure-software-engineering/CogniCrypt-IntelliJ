package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

interface IProjectOutputFinder
{
    Iterable<File> GetOutputFiles() throws OperationNotSupportedException, IOException, CogniCryptException;

    Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options) throws OperationNotSupportedException, IOException, CogniCryptException;
}

