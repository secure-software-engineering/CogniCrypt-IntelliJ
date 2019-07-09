package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.results.ui.CogniCryptResultWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Custom class that does not implement or utilize {@link ToolWindowFactory}. Reason is that {@link ToolWindowFactory} (by
 * design) loses information about the model classes that hold the Java-Swing components.
 */
public final class CogniCryptToolWindowFactory
{
    private static ContentFactory ContentFactory = com.intellij.ui.content.ContentFactory.SERVICE.getInstance();

    public static IReadOnlyCollection<ICogniCryptWindowBase> CreateToolWindow(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        toolWindow.setStripeTitle("CogniCrypt");
        toolWindow.setIcon(IconLoader.getIcon("/icons/cognicrypt.png"));

        List<ICogniCryptWindowBase> models =  new ArrayList<>();

        // Error Window
        ICogniCryptWindowBase resultsWindow = CogniCryptResultWindowFactory.CreateWindow(toolWindow, project);
        Content errorWindowContent = ContentFactory.createContent(resultsWindow.GetContent(), "Analysis Results", false);
        toolWindow.getContentManager().addContent(errorWindowContent);

        //Other Windows below...

        // Do NOT change order
        models.add(resultsWindow);
        return new ReadOnlyCollection<>(models);
    }
}

