package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

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
