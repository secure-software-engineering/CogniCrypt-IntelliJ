package de.fraunhofer.iem.icognicrypt.ui;

import de.fraunhofer.iem.icognicrypt.Constants;

import javax.swing.*;
import java.awt.*;

public class MessageBox
{
    public static void Show(String message, int messageOption){
        Show(message, messageOption, null);
    }

    public static void Show(String message, int messageOption, Component parent){
        JOptionPane.showMessageDialog(parent, message, "CogniCrypt", messageOption);
    }
}
