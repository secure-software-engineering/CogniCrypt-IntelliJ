package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class CogniCryptAction extends AnAction
{
    protected boolean VisibilityTriggeredByEnabledState = false;

    public CogniCryptAction(){
        super();
    }
    public CogniCryptAction(Icon icon){
        super(icon);
    }

    public CogniCryptAction(@Nullable String text){
        super(text);
    }

    public CogniCryptAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    protected final void SetVisibility(Presentation presentation, boolean visibility){
        if (VisibilityTriggeredByEnabledState)
            presentation.setEnabledAndVisible(visibility);
        else
            presentation.setEnabled(visibility);
    }
}
