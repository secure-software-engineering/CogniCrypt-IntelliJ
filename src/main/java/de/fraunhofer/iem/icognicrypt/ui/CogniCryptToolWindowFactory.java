package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptErrorWindow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Custom class that does not implement or utilize {@link ToolWindowFactory}. Reason is that {@link ToolWindowFactory} (by
 * design) loses information about the model classes that hold the Java-Swing components.
 */
public class CogniCryptToolWindowFactory
{

    private static ContentFactory ContentFactory = com.intellij.ui.content.ContentFactory.SERVICE.getInstance();

    public static IReadOnlyCollection<CogniCryptWindowBase> CreateToolWindow(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        System.out.println("Window Factory Thread: " + Thread.currentThread().getId());

        List<CogniCryptWindowBase> models =  new ArrayList<>();
        CogniCryptWindowBase errorWindow = new CogniCryptErrorWindow(toolWindow);

        // Error Window
        Content errorWindowContent = ContentFactory.createContent(errorWindow.GetContent(), "Errors", false);
        toolWindow.getContentManager().addContent(errorWindowContent);
        models.add(errorWindow);

        //Other Windows below...

        return new ReadOnlyCollection<>(models);
    }
}

