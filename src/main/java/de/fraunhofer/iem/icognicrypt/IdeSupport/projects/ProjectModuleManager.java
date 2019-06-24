package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;

import java.util.ArrayList;
import java.util.List;

public class ProjectModuleManager
{
    private GradleSettings _settings;

    public ProjectModuleManager(GradleSettings settings){

        _settings = settings;
        RegisterEvents(_settings);
    }

    public Iterable<JavaModule> GetModules()
    {
        // TODO: add caching if settings have not been changed

        List<JavaModule> results = new ArrayList<>();
        for (String modulePath : _settings.GetModulePathsAbsolute())
        {
            try
            {
                JavaModule module = new JavaModule(modulePath);
                results.add(module);
            }
            catch (JavaModuleNotFoundException e)
            {
                continue;
            }
        }
        return results;
    }

    private void RegisterEvents(GradleSettings settings)
    {
        //TODO: Listen for changes
    }
}
