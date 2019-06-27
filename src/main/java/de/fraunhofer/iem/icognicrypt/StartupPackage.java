package de.fraunhofer.iem.icognicrypt;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.core.BackgroundPackage;
import org.jetbrains.annotations.NotNull;

/**
 * This class is the main entry point to CogniCrypt. When a project is loaded after the IDE was started we consider the IDE setup to be completed.
 * After that event this class will run the {@link CogniCryptPlugin} which initializes the all other components. The project listener will be destroyed after that.
 *
 * We cannot use {@link StartupActivity} because it gets fired every time a project is loaded, which may cause multiple instance of CogniCrypt.
 * We cannot use {@link PreloadingActivity} because it gets fired at an uncertain IDE state, depending on other background tasks
 *
 * Do not use this class for anything else.
 */
public class StartupPackage extends BackgroundPackage
{
    private static final Logger logger = Logger.getInstance(StartupPackage.class);

    public StartupPackage(){
        Title = "CogniCrypt Bootstrapper";
        CanCancelInit = false;
    }

    @Override
    protected void InitializeInBackground(ProgressIndicator indicator)
    {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();

        logger.info("Waiting for IDE to be ready...");
        connection.subscribe(ProjectManager.TOPIC, new ProjectManagerListener()
        {
            @Override
            public void projectOpened(@NotNull Project project)
            {
                if (project.isDefault())
                {
                    logger.info("IDE not yet initialized - Skipping default project");
                    return;
                }
                logger.info("IDE initialized - First Project loaded");

                // Create the service first time
                ServiceManager.getService(CogniCryptPlugin.class);
                connection.disconnect();
            }
        });
    }
}


