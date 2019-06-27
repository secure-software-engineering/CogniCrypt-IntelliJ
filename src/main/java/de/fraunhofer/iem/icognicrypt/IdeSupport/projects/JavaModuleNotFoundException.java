package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

public class JavaModuleNotFoundException extends Exception
{
    public JavaModuleNotFoundException(String path){
        super(path);
    }
}
