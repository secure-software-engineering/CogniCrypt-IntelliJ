package de.fraunhofer.iem.icognicrypt.core.Dialogs;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.nio.file.Path;

public class DialogHelper
{
    public static File ChooseSingleFileFromDialog(String title, FileFilter filter, Path defaultPath)
    {
        return ChooseFileSystemObjectFromDialog(title, JFileChooser.FILES_ONLY, false, filter, false, defaultPath);
    }

    private static File ChooseFileSystemObjectFromDialog(String title, int options, boolean multiSelect, FileFilter filter, boolean defaultFilter, Path defaultPath){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(options);
        fileChooser.setAcceptAllFileFilterUsed(defaultFilter);
        fileChooser.setMultiSelectionEnabled(multiSelect);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(defaultPath.toFile());
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
