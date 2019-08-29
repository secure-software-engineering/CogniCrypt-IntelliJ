package de.fraunhofer.iem.icognicrypt.ui.multipleOutputFilesDialog;

import de.fraunhofer.iem.icognicrypt.core.Collections.Linq;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MultipleOutputFilesDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _buttonOK;
    private JButton _buttonCancel;
    private JCheckBox _saveCheckBox;
    private JList _filesListBox;
    private JXLabel _descriptionLabel;
    private JButton _buttonChooseDialog;
    private OutputFilesDialogResult _result;
    private Dimension _sizeConstraint = new Dimension(420, 350);
    private Iterable<File> _selectedFiles;

    public Iterable<File> GetSelectedFiles()
    {
        return _selectedFiles;
    }

    public boolean GetSaveChoice()
    {
        return _saveCheckBox.isSelected();
    }

    private OutputFilesDialogResult getResult()
    {
        return _result;
    }

    public MultipleOutputFilesDialog(Iterable<File> files, Iterable<Integer> preSelectedIndices){
        this();
        DefaultListModel model = new DefaultListModel();
        _filesListBox.setModel(model);

        for (File file : files)
            model.addElement(file);

        if (!Linq.any(preSelectedIndices))
            _filesListBox.getSelectionModel().addSelectionInterval(0, Linq.count(files) -1);
        else
        {
            //var t = new HashSet<Integer>(Linq.toList(preSelectedIndices));
        }
    }


    public MultipleOutputFilesDialog(Iterable<File> files)
    {
        this(files, Collections.EMPTY_LIST);
    }

    MultipleOutputFilesDialog()
    {
        setPreferredSize(_sizeConstraint);
        setResizable(false);
        setModal(true);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(_buttonOK);

        setTitle("CogniCrypt");
        _filesListBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        _descriptionLabel.setText("CogniCrypt found multiple files to analyze. Please select the file you wish to analyze.");
        _descriptionLabel.setLineWrap(true);

        RegisterEvents();
    }

    public OutputFilesDialogResult ShowDialog()
    {
        pack();
        setVisible(true);
        return getResult();
    }

    protected void onOK()
    {
        // add your code here
        _result = OutputFilesDialogResult.OK;
        dispose();
    }

    protected void onChooseDialog()
    {
        _result = OutputFilesDialogResult.ChooseManually;
        dispose();
    }

    protected void onCancel()
    {
        // add your code here if necessary
        _result = OutputFilesDialogResult.Abort;
        dispose();
    }

    private void RegisterEvents()
    {
        _buttonOK.addActionListener(e -> onOK());
        _buttonCancel.addActionListener(e -> onCancel());
        _buttonChooseDialog.addActionListener(e -> onChooseDialog());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        _filesListBox.addListSelectionListener(e ->
           {
               if (!e.getValueIsAdjusting())
                   _selectedFiles = _filesListBox.getSelectedValuesList();
           });
    }

    public enum OutputFilesDialogResult{
        OK,
        Abort,
        ChooseManually
    }

    public static void main(String[] args)
    {
        Collection<File> files = new ArrayList<>();
        files.add(new File("C:\\Test.txt"));
        files.add(new File("C:\\Test2.txt"));

        MultipleOutputFilesDialog dialog = new MultipleOutputFilesDialog(files);
        OutputFilesDialogResult result = dialog.ShowDialog();
        Iterable<File> f = dialog.GetSelectedFiles();
        System.exit(0);
    }
}
