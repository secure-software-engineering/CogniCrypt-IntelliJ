package de.fraunhofer.iem.icognicrypt.actions.Tests;

import com.intellij.openapi.actionSystem.AnActionEvent;
import de.fraunhofer.iem.icognicrypt.actions.Actions;
import de.fraunhofer.iem.icognicrypt.actions.CogniCryptAction;
import org.jetbrains.annotations.NotNull;

public class ExecuteOtherAction extends CogniCryptAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Actions.TryExecute("ICognicrypt.ExecuteAnalysis", e.getDataContext());
    }
}
