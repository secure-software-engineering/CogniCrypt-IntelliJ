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
import java.util.HashMap;

public class SupportedLanguagesUtils
{
    public ReadOnlyPriorityList<SupportedLanguage> SupportedLanguages = new ReadOnlyPriorityList<>(SupportedLanguage.values());

    private final HashMap<String, String> _supportedLanguageExtensionMapping = new HashMap<>();

    private SupportedLanguagesUtils()
    {
        for (SupportedLanguage supportedLanguage : SupportedLanguages){
            _supportedLanguageExtensionMapping.put(supportedLanguage.GetId(), GetFileExtensionInternal(supportedLanguage, true));
        }
    }

    public static boolean RequiresSourceFileNameModification(SupportedLanguage supportedLanguage){
        switch (supportedLanguage){
            case Java:
                return false;
            case Kotlin:
                return true;
            default:
                return false;
        }
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

    public String GetFileExtension(SupportedLanguage supportedLanguage){
        return GetFileExtensionInternal(supportedLanguage, false);
    }

    private String GetFileExtensionInternal(SupportedLanguage supportedLanguage, boolean initializing)
    {
        if (!initializing)
            return _supportedLanguageExtensionMapping.get(supportedLanguage.GetId());
        Language language = Language.findLanguageByID(supportedLanguage.GetId());
        return language.getAssociatedFileType().getDefaultExtension();
    }
}

