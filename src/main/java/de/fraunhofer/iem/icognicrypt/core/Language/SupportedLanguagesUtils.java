package de.fraunhofer.iem.icognicrypt.core.Language;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import javaLinq.Linq;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyPriorityList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class SupportedLanguagesUtils
{
    public ReadOnlyPriorityList<SupportedLanguage> SupportedLanguages = new ReadOnlyPriorityList<>(SupportedLanguage.values());

    private SupportedLanguagesUtils()
    {
    }

    public boolean IsSupported(Language language)
    {
        if (language == null)
            return false;
        return Linq.any(SupportedLanguages, lang -> lang.GetId().equals(language.getID()));
    }

    public Collection<File> GetSupportedSourceFiles(VirtualFile directory)
    {
        if (directory == null || !directory.isDirectory())
            throw new IllegalArgumentException();
        Collection<File> result = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(new File(directory.getPath()), null, true);

        for (File file: files)
        {
            FileType ft = FileTypeManager.getInstance().getFileTypeByFileName(file.getName());
            Language language = LanguageUtil.getFileTypeLanguage(ft);

            if (!IsSupported(language))
                continue;
            result.add(file);
        }
        return result;
    }
}

