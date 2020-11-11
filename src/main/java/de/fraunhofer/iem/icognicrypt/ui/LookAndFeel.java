package de.fraunhofer.iem.icognicrypt.ui;
import org.apache.commons.lang.SystemUtils;

import javax.swing.*;

public class LookAndFeel
{
    private LookAndFeel(){
        SetNativeLookAndFeel();
    }

    private static void SetNativeLookAndFeel()
    {
        if (SystemUtils.IS_OS_WINDOWS)
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
    }
}
