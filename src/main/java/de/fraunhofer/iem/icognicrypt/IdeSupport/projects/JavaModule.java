package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IHasOutputManager;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IHasOutputs;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputManager;

import java.nio.file.*;

// TODO: There already is a Module implementation "ModuleImpl". Try to it with this.
public class JavaModule implements IHasOutputManager
{
    // TODO: Apparently libraries are build in a folder called aar. We need to decide if we want to check for the too, or just support apk files.
    private static final String RelativeBuildPath = "build\\outputs\\apk\\";
    private static final Logger logger = Logger.getInstance(JavaModule.class);

    private Path _path;

    private IHasOutputs _outputManager;

    public IHasOutputs GetOutputManager() { return _outputManager;}

    public JavaModule(String path) throws JavaModuleNotFoundException
    {
        Path realPath = Paths.get(path);
        if (!realPath.toFile().exists()) throw new JavaModuleNotFoundException(path);
        _path = realPath;

        _outputManager = new JavaModuleOutputManager(this);
        _outputManager.InvalidateOutput();

    }

    private class JavaModuleOutputManager extends OutputManager
    {
        private final JavaModule _owner;

        public JavaModuleOutputManager(JavaModule owner)
        {
            _owner = owner;
        }

        @Override
        public void InvalidateOutput()
        {
            _owner.logger.info("Invalidating Java Module " + _path);
            DebugJson = OutputJson.Deserialize(Paths.get(_path.toString(), _owner.RelativeBuildPath, "debug\\output.json").toString());
            ReleaseJson = OutputJson.Deserialize(Paths.get(_path.toString(), _owner.RelativeBuildPath, "release\\output.json").toString());
        }
    }
}

