package de.fraunhofer.iem.icognicrypt.ui.ToolWindow;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

final class CogniCryptToolWindowManager implements ProjectComponent, ICogniCryptToolWindowManager
{
    // We cannot use weak references here because the model is nowhere is stored and therefore would get GCed.
    private final HashMap<Integer,ICogniCryptWindowBase> _models = new HashMap<>();
    private WeakReference<ToolWindow> _toolWindow;

    public void RegisterToolWindow(ToolWindow window){
        _toolWindow = new WeakReference<>(window);
    }

    public void RegisterModel(ToolWindowModelType type, ICogniCryptWindowBase model) throws CogniCryptException
    {
        Integer typeNumber = new Integer(type.getValue());
        if (_models.containsKey(typeNumber))
            throw new CogniCryptException("Tool Window Model already registered");
        _models.put(typeNumber, model);
    }

    public ICogniCryptWindowBase GetModel(ToolWindowModelType modelType) throws CogniCryptException
    {
        return GetModel(modelType, ICogniCryptWindowBase.class);
    }

    public <T extends ICogniCryptWindowBase> T GetModel(ToolWindowModelType modelType, Class<T> type) throws CogniCryptException
    {
        Integer typeNumber = new Integer(modelType.getValue());
        if (!_models.containsKey(typeNumber))
            return null;

        ICogniCryptWindowBase model = _models.get(typeNumber);
        if (model == null){
            _models.remove(typeNumber);
            return null;
        }

        if (type.isInstance(model))
            return type.cast(model);
        throw new CogniCryptException("Cannot cast type: " + model.getClass() + " to type: " + type);
    }

    @Override
    public void projectClosed()
    {
        _models.clear();
        disposeComponent();
    }
}


