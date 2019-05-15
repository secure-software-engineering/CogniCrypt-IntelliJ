package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import de.fraunhofer.iem.icognicrypt.ui.SettingsDialog;

public class UpdateSettings extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.pack();
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setSize(550,150);
        settingsDialog.setVisible(true);
    }
}
