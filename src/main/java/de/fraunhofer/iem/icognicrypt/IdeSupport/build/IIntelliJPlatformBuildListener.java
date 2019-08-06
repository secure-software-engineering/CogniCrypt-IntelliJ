package de.fraunhofer.iem.icognicrypt.IdeSupport.build;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;

public interface IIntelliJPlatformBuildListener
{
    Topic<IIntelliJPlatformBuildListener> TOPIC = Topic.create("IntelliJ Platform Build Broker", IIntelliJPlatformBuildListener.class);

    default void buildFinished(Project project){ }
}
