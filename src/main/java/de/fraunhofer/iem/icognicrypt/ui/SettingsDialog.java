package de.fraunhofer.iem.icognicrypt.ui;

import de.fraunhofer.iem.icognicrypt.actions.IcognicryptSettings;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ruleDirectory;
    private JButton selectDirectory;
    IcognicryptSettings settings;

    public SettingsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Setup ICogniCrypt");
        settings = IcognicryptSettings.getInstance();

        ruleDirectory.setText(settings.getRulesDirectory());

        selectDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ruleDirectory.setText(fileSelector(settings.getRulesDirectory()));
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here

        settings.setRulesDirectory(ruleDirectory.getText());

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    //Accept users file or folder selection and send return value
    private String fileSelector(String path) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File(path));

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        else
            return path;
    }
}
