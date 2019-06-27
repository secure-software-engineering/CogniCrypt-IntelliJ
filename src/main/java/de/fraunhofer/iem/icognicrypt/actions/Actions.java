package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;

import java.awt.*;

public class Actions
{
    public static boolean CanExecute(String id, DataContext dataContext)
    {
        return CanExecute(id, dataContext, ActionPlaces.UNKNOWN);
    }

    public static boolean CanExecute(String id, Component component)
    {
        return CanExecute(id, DataManager.getInstance().getDataContext(component), ActionPlaces.UNKNOWN);
    }

    public static boolean CanExecute(String id, Component component, String actionPlaces)
    {
        return CanExecute(id, DataManager.getInstance().getDataContext(component), actionPlaces);
    }

    public static boolean CanExecute(String id, DataContext dataContext, String actionPlaces)
    {
        ActionManager am = ActionManager.getInstance();
        return CanExecute(am, am.getAction(id), dataContext, actionPlaces);
    }

    private static boolean CanExecute(ActionManager am, AnAction action, DataContext dataContext, String actionPlaces)
    {
        AnActionEvent event = new AnActionEvent(null, dataContext, actionPlaces, new Presentation(), am, 0);
        action.update(event);
        return event.getPresentation().isEnabled();
    }

    public static void Execute(String id, Component component)
    {
        Execute(id, DataManager.getInstance().getDataContext(component));
    }

    public static void Execute(String id, Component component, String actionPlaces)
    {
        Execute(id, DataManager.getInstance().getDataContext(component), actionPlaces);
    }

    public static void Execute(String id, DataContext dataContext)
    {
        Execute(id, dataContext, ActionPlaces.UNKNOWN);
    }

    public static void Execute(String id, DataContext dataContext, String actionPlaces)
    {
        ActionManager am = ActionManager.getInstance();
        Execute(am, am.getAction(id), dataContext, actionPlaces);
    }

    public static void Execute(AnAction action, DataContext dataContext, String actionPlaces)
    {
        ActionManager am = ActionManager.getInstance();
        Execute(am, action, dataContext, actionPlaces);
    }

    private static void Execute(ActionManager am, AnAction action, DataContext dataContext, String actionPlaces)
    {
        action.actionPerformed(new AnActionEvent(null, dataContext, actionPlaces, new Presentation(), am, 0));
    }

    public static void TryExecute(String id, Component component)
    {
        if (CanExecute(id, component)) Execute(id, DataManager.getInstance().getDataContext(component));
    }

    public static void TryExecute(String id, Component component, String actionPlaces)
    {
        if (CanExecute(id, component, actionPlaces)) Execute(id, DataManager.getInstance().getDataContext(component), actionPlaces);
    }

    public static void TryExecute(String id, DataContext dataContext)
    {
        if (CanExecute(id, dataContext)) Execute(id, dataContext, ActionPlaces.UNKNOWN);
    }

    public static void TryExecute(String id, DataContext dataContext, String actionPlaces)
    {
        ActionManager am = ActionManager.getInstance();
        AnAction action = am.getAction(id);
        if (CanExecute(am, action, dataContext, actionPlaces)) Execute(am, action, dataContext, actionPlaces);
    }

    public static void TryExecute(AnAction action, DataContext dataContext, String actionPlaces)
    {
        ActionManager am = ActionManager.getInstance();
        if (CanExecute(am, action, dataContext, actionPlaces)) Execute(am, action, dataContext, actionPlaces);
    }
}
