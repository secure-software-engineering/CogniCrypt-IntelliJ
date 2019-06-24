package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;

import java.util.ArrayList;
import java.util.List;


// TODO: Implement a signletone class 'ProjectManager' that has this instance (or maybe the settings.gradle ???) as property. The 'ProjectManager' also holds the IdeaWorkspace instance.
// TODO: See what happens to settings.gradle and this instance if the project in the IDE is changed. Invalidate state then.
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
