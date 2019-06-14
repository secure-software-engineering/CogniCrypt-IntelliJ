package Gradle;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public abstract class GradleSettingsScript extends Script
{
    private boolean _initialized;
    private File _file;

    private Iterable<String> _includedPaths;

    public static GradleSettingsScript Find(String rootPath) throws IOException
    {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass("Gradle.GradleSettingsScript");

        GroovyShell shell = new GroovyShell(GradleSettingsScript.class.getClassLoader(), new Binding(), config);
        Path settingsPath = Paths.get(rootPath, "settings.gradle");
        File settingsFile = settingsPath.toFile();
        if (!settingsFile.exists())
            throw new FileNotFoundException("settings.gradle was not found");
        GradleSettingsScript script = (GradleSettingsScript) shell.parse(settingsFile);
        script._file = settingsFile;
        return script;
    }

    public void include(String[] values)
    {
        HashSet<String> hashSet = new HashSet<String>();
        for (String value : values)
            hashSet.add(value);
        _includedPaths = hashSet;
        _initialized = true;
    }

    public Iterable<String> GetProjectPaths() throws OperationNotSupportedException
    {
        return InternalGetProjectPaths();
    }

    public Iterable<String> GetProjectPathsAbsolute()throws OperationNotSupportedException
    {
        return InternalGetProjectPaths();
    }

    public File GetFile(){ return _file; }

    private Iterable<String> InternalGetProjectPaths() throws OperationNotSupportedException
    {
        if (!_initialized) throw new OperationNotSupportedException("settings.gradle script must be run at least once");
        return null;
    }
}
