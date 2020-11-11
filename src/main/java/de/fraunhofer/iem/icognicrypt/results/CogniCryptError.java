package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.core.Language.JvmClassNameUtils;

import java.io.File;

public class CogniCryptError {

    private String _errorMessage;
    private String _className;
    private int _lineNumer;

    private String _filePath;

    public CogniCryptError(String error, String className, int line) {
        _errorMessage = error;
        _className = className;
        _lineNumer = line;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public String getClassName(){
        return _className;
    }

    public int getLine(){
        return _lineNumer;
    }

    private String getFilePath(){
        return _filePath;
    }

    private void setFilePath(String filePath){
        _filePath = filePath;
    }

    public static File getSourceFileOfError(CogniCryptError error, Project project)
    {
        String path = error.getFilePath();
        if (path == null)
        {
            path = JvmClassNameUtils.FindFileFromFullyQualifiedName(error.getClassName(), project);
            error.setFilePath(path);
        }
        if (path == null)
            return null;
        File sourceFile = new File(path);
        if (!sourceFile.exists())
            return null;
        return sourceFile;
    }

}
