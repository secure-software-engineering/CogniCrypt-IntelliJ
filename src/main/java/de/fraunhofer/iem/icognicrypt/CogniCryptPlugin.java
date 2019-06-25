package de.fraunhofer.iem.icognicrypt;

import com.intellij.application.Topics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.project.*;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.Context;

public class CogniCryptPlugin implements BaseComponent
{

    public CogniCryptPlugin()
    {

    }

    @Override
    public void initComponent()
    {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();

        connection.subscribe(ProjectManager.TOPIC, new ProjectManagerListener()
        {
            @Override
            public void projectOpened(@NotNull Project project)
            {
                System.out.println(project);
            }
        });

    }

}
