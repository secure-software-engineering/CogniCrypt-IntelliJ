package de.fraunhofer.iem.icognicrypt.IdeSupport.build;

import com.android.tools.idea.gradle.project.build.*;
import com.android.tools.idea.project.AndroidProjectBuildNotifications;
import com.intellij.compiler.server.BuildManagerListener;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import de.fraunhofer.iem.icognicrypt.IdeSupport.platform.IIdePlatformProvider;
import javaLinq.Linq;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

// TODO: Check if the broker architecture can be eliminated once AS build 191 is available such as ProjectTaskListener or other topics.
class IntelliJPlatformBuildBroker implements ProjectComponent {

    private Project _project;
    private IIdePlatformProvider _platformProvider;
    private MessageBus _messageBus;
    private MessageBusConnection _connection;

    private int _gradleCount;

    public IntelliJPlatformBuildBroker(Project project, IIdePlatformProvider platformProvider)
    {
        _platformProvider = platformProvider;
        _project = project;
        _messageBus = _project.getMessageBus();
        _connection =  _messageBus.connect();
    }

    @Override
    public void initComponent()
    {
        switch (_platformProvider.GetRunningPlatform())
        {
            case Unknown:
                break;
            case IntelliJ:
                _connection.subscribe(BuildManagerListener.TOPIC, new BuildManagerListener()
                {
                    @Override
                    public void buildFinished(@NotNull Project project, @NotNull UUID sessionId, boolean isAutomake)
                    {
                        Publish();
                    }
                });
                break;
            case AndroidStudio:
                AndroidProjectBuildNotifications.subscribe(_project, context ->
                {
                    if (context instanceof GradleBuildContext){
                        GradleBuildContext gradleBuildContext = (GradleBuildContext) context;

                        if (Linq.any(gradleBuildContext.getBuildResult().getTasks(), task -> {
                            // TODO: Check if :assemble is a 3.5 thing
                            if(task.contains("assemble") || task.equals("clean"))
                                return true;
                            return false;
                        }))
                        {
                            // TODO: Remove if statement once update to newer AS version. The fire bug twice is fixed in newer builds
                            if (++_gradleCount % 2 == 0)
                                Publish();
                        }
                    }
                });
                break;
        }
    }

    private void Publish(){
        _messageBus.syncPublisher(IIntelliJPlatformBuildListener.TOPIC).buildFinished(_project);
    }

    @Override
    public void disposeComponent()
    {
        _connection.disconnect();
        _messageBus = null;
        _platformProvider = null;
        _project = null;
    }
}

