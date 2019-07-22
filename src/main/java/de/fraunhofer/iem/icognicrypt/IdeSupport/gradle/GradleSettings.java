package de.fraunhofer.iem.icognicrypt.IdeSupport.gradle;

import com.google.common.collect.Lists;
import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.core.FileSystem.FileModifiedWatcher;
import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GradleSettings
{
    private static final Logger logger = Logger.getInstance(GradleSettings.class);

    private GradleSettingsScript _script;

    private Path _projectPath;

    private Iterable<String> _projectModules;

    public GradleSettings(Path projectPath) throws IOException, OperationNotSupportedException
    {
        _projectPath = projectPath;

        // TODO: Once we can support file watching enable it again
//        GradleSettingsFileWatcher watcher = new GradleSettingsFileWatcher(this, _projectPath);
//        watcher.Listen();

        Invalidate();
    }

    public void Invalidate() throws IOException, OperationNotSupportedException
    {
        _script = GradleSettingsScript.Find(_projectPath);
        _script.RunScript();
        _projectModules = _script.GetModules();
        logger.info("Project modules found: " + _projectModules);
    }

    public Iterable<String> GetModulePaths()
    {
        return InternalGetModulePaths(false);
    }

    public Iterable<String> GetModulePathsAbsolute()
    {
        return InternalGetModulePaths(true);
    }

    private Iterable<String> InternalGetModulePaths(boolean absolute)
    {
        if (!absolute)
            return Lists.newArrayList(_projectModules);

        ArrayList<String> paths = new ArrayList<>();
        for (String module : _projectModules)
        {
            String trimmedName = StringTrimming.Trim(module, ':');

            if (!absolute)
                paths.add(trimmedName);
            else
            {
                Path path = Paths.get(_projectPath.toString(), trimmedName);
                paths.add(path.toString());
            }
        }
        return paths;
    }


    // TODO: Once we can support file watching enable it again
//    private class GradleSettingsFileWatcher extends FileModifiedWatcher
//    {
//        private final GradleSettings _settings;
//
//        public GradleSettingsFileWatcher(GradleSettings settings, Path path) throws IOException
//        {
//            super(path, "settings.gradle");
//            if (settings == null)
//                throw new IllegalArgumentException("settings cannot be null");
//            _settings = settings;
//        }
//
//        @Override
//        protected void OnModified()
//        {
//            System.out.println("123");
//            try
//            {
//                _settings.Invalidate();
//            }
//            catch (IOException e)
//            {
//            }
//            catch (OperationNotSupportedException e)
//            {
//            }
//        }
//    }

}
