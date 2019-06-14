package Core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration
{
    private static Configuration _instance;

    private String _projectPath;

    // Creates and returns a singletone instance of the configuration
    public static Configuration GetInstance()
    {
        if (_instance == null) return _instance = new Configuration();
        return _instance;
    }

    public void SetProjectPath(String path) throws IOException
    {
        if (!Files.exists(Paths.get(path)))
                throw new IOException("${path} not found");
        path = path.replaceAll("\\ $", "");
        path += "\\";
        _projectPath = path;
    }

    public String GetProjectPath()
    {
        if (_projectPath == null)
            throw new IllegalStateException("The project path is not set yet");
        return _projectPath;
    }

    private Configuration()
    {
    }
}
