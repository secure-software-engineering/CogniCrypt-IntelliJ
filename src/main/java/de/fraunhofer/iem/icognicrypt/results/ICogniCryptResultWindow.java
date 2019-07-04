package de.fraunhofer.iem.icognicrypt.results;

import de.fraunhofer.iem.icognicrypt.ui.ICogniCryptWindowBase;

public interface ICogniCryptResultWindow extends ICogniCryptWindowBase
{
    void SetSearchText(String text);

    ICogniCryptResultTableModel GetTableModel();
}
