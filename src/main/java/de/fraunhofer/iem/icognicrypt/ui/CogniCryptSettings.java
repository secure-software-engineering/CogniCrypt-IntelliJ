package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CogniCryptSettings implements Configurable {
    private JTextField rulesDirTextfield;
    private boolean isModified;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "CogniCrypt";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel contentPane = new JPanel();
        contentPane.add(new JLabel("CrySL Rules Directory"));
        rulesDirTextfield = new JTextField();
        contentPane.add(rulesDirTextfield);
        JButton selectDirButton = new JButton("...");
        contentPane.add(rulesDirTextfield);
        contentPane.add(selectDirButton);
        CogniCryptSettingsPersistentComponent settings = CogniCryptSettingsPersistentComponent.getInstance();
        rulesDirTextfield.setText(settings.getRulesDirectory());
        selectDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rulesDirTextfield.setText(openFileChooserDialog(settings.getRulesDirectory(),contentPane));
            }
        });
        return contentPane;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        CogniCryptSettingsPersistentComponent settings = CogniCryptSettingsPersistentComponent.getInstance();
        settings.setRulesDirectory(rulesDirTextfield.getText());
    }

    //Accept users file or folder selection and send return value
    private String openFileChooserDialog(String path, JPanel contentPane) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new java.io.File(path));
        int returnValue = fileChooser.showOpenDialog(contentPane);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            isModified = true;
            return fileChooser.getSelectedFile().getPath();
        }
        else {
            return path;
        }
    }
}
