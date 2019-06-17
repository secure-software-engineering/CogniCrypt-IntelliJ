package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.fraunhofer.iem.icognicrypt.Constants;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import sun.java2d.loops.ProcessPath;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

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
        selectDirButton.addActionListener(e -> rulesDirTextfield.setText(openFileChooserDialog(settings.getRulesDirectory(),contentPane)));
        return contentPane;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() {
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
            String newPath = fileChooser.getSelectedFile().getPath();
            if(isValidCrySLRuleDirectory(newPath)){
                return newPath;
            } else {
                showDownloadCrySLRulesDialog(contentPane, newPath);
                return path;
            }
        }
        else {
            return path;
        }
    }

    private void showDownloadCrySLRulesDialog(JPanel contentPane, String newPath) {
        JOptionPane.showMessageDialog(contentPane,
                "No .cryptslbin files found in "+ newPath+" \nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK,
                "CrySL Rule Selection Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static boolean isValidCrySLRuleDirectory(String path){
        File rulesDir = new File(path);
        if(!rulesDir.exists()){
            return false;
        }

        if(!rulesDir.isDirectory()) {
            return false;
        }

        return rulesDir.listFiles((dir, filename) -> filename.endsWith(Constants.CRYSL_BIN_EXTENSION)).length != 0;
    }
}
