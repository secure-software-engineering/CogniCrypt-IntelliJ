package de.fraunhofer.iem.icognicrypt.core.Dialogs;

import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

public class DialogHelper
{

    private static final Function<File, File> _defaultResultHandler = selected -> selected;

    public static String ChooseSingleDirectoryFromDialog(String title, Path defaultPath)
    {
        return ChooseSingleDirectoryFromDialog(title, defaultPath, _defaultResultHandler);
    }

    public static String ChooseSingleDirectoryFromDialog(String title, Path defaultPath, Function<File, File> resultFunction)
    {
        File selectedDirectory = ChooseSingleDirectoryFromDialog(title, defaultPath, null, resultFunction);
        return selectedDirectory.getPath();
    }

    public static File ChooseSingleDirectoryFromDialog(String title, Path defaultPath, Component parent, Function<File, File> resultFunction)
    {
        Consumer<JFileChooser> setup = dialog -> {
            dialog.setAcceptAllFileFilterUsed(false);
            dialog.setMultiSelectionEnabled(false);
        };
        return ChooseFileSystemObjectFromDialog(title, JFileChooser.DIRECTORIES_ONLY, defaultPath, parent, setup, resultFunction);
    }


    public static File ChooseSingleFileFromDialog(String title, FileFilter filter, Path defaultPath)
    {
        return ChooseSingleFileFromDialog(title, filter, defaultPath, null, _defaultResultHandler);
    }

    public static File ChooseSingleFileFromDialog(String title, FileFilter filter, Path defaultPath, Function<File, File> resultFunction)
    {
        return ChooseSingleFileFromDialog(title, filter, defaultPath, null, resultFunction);
    }

    public static File ChooseSingleFileFromDialog(String title, FileFilter filter, Path defaultPath, Component parent)
    {
        return ChooseSingleFileFromDialog(title, filter, defaultPath, parent, _defaultResultHandler);
    }

    public static File ChooseSingleFileFromDialog(String title, FileFilter filter, Path defaultPath, Component parent, Function<File, File> resultFunction)
    {
        Consumer<JFileChooser> setup = dialog -> {
            dialog.setAcceptAllFileFilterUsed(false);
            dialog.setMultiSelectionEnabled(false);
            dialog.addChoosableFileFilter(filter);
        };
        return ChooseFileSystemObjectFromDialog(title, JFileChooser.FILES_ONLY, defaultPath, parent, setup, resultFunction);
    }

    private static File ChooseFileSystemObjectFromDialog(String title, int options, Path defaultPath, Component parent,
                                                         Consumer<JFileChooser> setupFunction, Function<File, File> handleResult){
        return ChooseSingleFromDialog(title, options, defaultPath, parent, setupFunction, handleResult);
    }

    private static File ChooseSingleFromDialog(String title, int options, Path defaultPath, Component parent, Consumer<JFileChooser> setupFunction, Function<File, File> handleResult)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(options);
        fileChooser.setCurrentDirectory(defaultPath.toFile());
        setupFunction.accept(fileChooser);

        int returnValue = fileChooser.showOpenDialog(parent);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return handleResult.apply(fileChooser.getSelectedFile());
        }
        return null;
    }
}
