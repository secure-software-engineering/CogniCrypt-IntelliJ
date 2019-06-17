package de.fraunhofer.iem.icognicrypt.IdeSupport.gradle;

import afu.org.checkerframework.checker.oigj.qual.O;
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
import java.util.ArrayList;
import java.util.HashSet;

public abstract class GradleSettingsScript extends Script
{
    private boolean _initialized;

    private HashSet<String> _modules = new HashSet<>();

    public static GradleSettingsScript Find(Path rootPath) throws IOException
    {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass("de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettingsScript");

        GroovyShell shell = new GroovyShell(GradleSettingsScript.class.getClassLoader(), new Binding(), config);

        Path settingsPath = Paths.get(rootPath.toString(), "settings.gradle");
        File settingsFile = settingsPath.toFile();
        if (!settingsFile.exists())
            throw new FileNotFoundException("settings.gradle was not found");
        GradleSettingsScript script = (GradleSettingsScript) shell.parse(settingsFile);
        return script;
    }

    public void include(String[] values)
    {
        for (String value : values)
            _modules.add(value);
        _initialized = true;
    }

    public void RunScript()
    {
        _modules.clear();
        System.out.println("runScript");
        run();
    }

    public Iterable<String> GetModules() throws OperationNotSupportedException
    {
        if (!_initialized)
            throw new OperationNotSupportedException("settings.gradle script must be run at least once");
        return _modules;
    }
}
