package de.fraunhofer.iem.icognicrypt.core.crySL;

import de.fraunhofer.iem.icognicrypt.Constants;

import java.io.File;

public class CrySLHelper
{
    public static boolean isValidCrySLRuleDirectory(String path){
        File rulesDir = new File(path);
        if(!rulesDir.exists())
            return false;
        if(!rulesDir.isDirectory())
            return false;
        return rulesDir.listFiles((dir, filename) -> filename.endsWith(Constants.CRYSL_EXTENSION)).length != 0;
    }
}
