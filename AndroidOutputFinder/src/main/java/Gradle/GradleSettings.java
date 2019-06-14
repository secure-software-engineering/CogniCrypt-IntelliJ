package Gradle;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.nio.file.*;

import Core.Configuration;
import FileSystem.*;

public class GradleSettings
{
    private static GradleSettings _instance;

    private GradleSettingsScript _script;

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
        //_script = GradleSettingsScript.Find(Configuration.GetInstance().GetProjectPath());
        GradleSettingsFileWatcher watcher = new GradleSettingsFileWatcher(this, projectPath);
        watcher.Listen();
        Invalidate();
    }

    public void Invalidate() throws IOException
    {
        _script = GradleSettingsScript.Find(Configuration.GetInstance().GetProjectPath());
        _script.run();
    }


    private class GradleSettingsFileWatcher extends FileModifiedWatcher
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
    }

}
