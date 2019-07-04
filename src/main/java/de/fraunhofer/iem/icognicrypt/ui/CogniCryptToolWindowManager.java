package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectListener;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectManager;
import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class CogniCryptToolWindowManager extends CogniCryptProjectListener
{
    private WeakHashMap<ToolWindow, IReadOnlyCollection<ICogniCryptWindowBase>> _windowModelMapping = new WeakHashMap<>();
    private  WeakHashMap<Project,WeakReference<ToolWindow>> _projectWindowMapping = new WeakHashMap<>();


    public static final String CogniCryptWindowId = "ICogniCrypt.ToolWindow";

    public static final int ResultsView = 0;

    @Override
    public void OnProjectOpened(Project project)
    {
        ToolWindow window  = ToolWindowManager.getInstance(project).registerToolWindow(CogniCryptWindowId, false, ToolWindowAnchor.BOTTOM, true);
        IReadOnlyCollection<ICogniCryptWindowBase> models =  CogniCryptToolWindowFactory.CreateToolWindow(project, window);

        _projectWindowMapping.put(project, new WeakReference<>(window));
        _windowModelMapping.put(window, models);
    }

    @Override
    public void OnProjectClosed(Project project)
    {
        ToolWindow toolWindow = _projectWindowMapping.get(project).get();
        if (toolWindow != null)
            ToolWindowManager.getInstance(project).unregisterToolWindow(CogniCryptWindowId);
        if (_windowModelMapping.containsKey(toolWindow))
            _windowModelMapping.remove(toolWindow);
    }

    public ToolWindow GetToolWindow(Project project) throws CogniCryptException
    {
        if (project == null || project.isDefault() || !project.isInitialized() || project.isDisposed())
            throw new CogniCryptException("The given project must not be null, a default project, uninitialized or disposed");

        ToolWindow toolWindow = _projectWindowMapping.get(project).get();

        if (toolWindow == null)
            throw new CogniCryptException("CogniCrypt ToolWindow was not initialized by the manager");
        return toolWindow;
    }

    public <T extends ICogniCryptWindowBase> T GetWindowModel(ToolWindow toolWindow, int viewIndex, Class<T> type) throws CogniCryptException
    {
        IReadOnlyCollection<ICogniCryptWindowBase> list = _windowModelMapping.get(toolWindow);
        if (list == null){
            throw new CogniCryptException("CogniCrypt ToolWindow was not initialized by the manager");
        }

        ICogniCryptWindowBase model = list.Get(viewIndex);
        if (model == null)
            return null;

        if (type.isInstance(model))
            return type.cast(model);
        throw new CogniCryptException("Cannot cast type: " + model.getClass() + " to type: " + type);
    }

    @Override
    public void dispose()
    {
        ServiceManager.getService(CogniCryptProjectManager.class).UnSubscribe(this);
    }
}
