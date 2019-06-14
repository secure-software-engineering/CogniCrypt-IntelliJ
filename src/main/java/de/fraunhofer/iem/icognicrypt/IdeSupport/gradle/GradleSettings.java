package de.fraunhofer.iem.icognicrypt.IdeSupport.gradle;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.nio.file.Path;

public class GradleSettings
{
    private static GradleSettings _instance;

    private GradleSettingsScript _script;

    private Path _projectPath;

    // Creates and returns a singletone instance of the gradle settings
    public static GradleSettings GetInstance()
    {
        if (_instance == null)
            throw new NullPointerException("Gradle settings not set yet.");
        return _instance;
    }

    public GradleSettings(Path projectPath) throws IOException, OperationNotSupportedException
    {
        _instance = this;
        _projectPath = projectPath;

        // TODO: Once we can support file watching enable it again
        //GradleSettingsFileWatcher watcher = new GradleSettingsFileWatcher(this, _projectPath);
        //watcher.Listen();

        Invalidate();
    }

    public void Invalidate() throws IOException
    {
        _script = GradleSettingsScript.Find(_projectPath);
        _script.run();
    }


    // TODO: Once we can support file watching enable it again
    /* private class GradleSettingsFileWatcher extends FileModifiedWatcher
    {
        private final GradleSettings _settings;

        public GradleSettingsFileWatcher(GradleSettings settings, Path path) throws IOException
        {
            super(path, "settings.gradle");
            if (settings == null)
                throw new IllegalArgumentException("settings cannot be null");
            _settings = settings;
        }

        @Override
        protected void OnModified()
        {
            System.out.println("123");
            try
            {
                _settings.Invalidate();
            }
            catch (IOException e)
            {
            }
        }
    }*/

}
