package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import crypto.cryslhandler.CrySLModelReader;


public class Constants {

    public final static boolean AUTOMATIC_SCAN_ON_COMPILE = false;

    //public static final String DummyCrySLPath = "./CrySLRules/JCA";
    private static final ApplicationNamesInfo nameInfo = ApplicationNamesInfo.getInstance();
    static String productName = nameInfo.getFullProductName();
    static String current_version= ApplicationInfo.getInstance().getMajorVersion()+"."+ ApplicationInfo.getInstance().getMinorVersionMainPart();
    //public static final String DummyCrySLPath=System.getProperty("user.home")+"\\."+productName+current_version+"\\config\\plugins\\icognicrypt\\lib\\CrySLRules\\JCA";
    public static final String DummyCrySLPath= PathManager.getPluginsPath()+"\\icognicrypt\\lib\\CrySLRules\\JCA";;

    // TODO: This constant is not final in CryptoAnalysis
    public static final String CRYSL_BIN_EXTENSION = CrySLModelReader.cryslFileEnding;
    public static final String CRYSL_RULES_DOWNLOADLINK = "https://soot-build.cs.uni-paderborn.de/nexus/repository/soot-release/de/darmstadt/tu/crossing/JavaCryptographicArchitecture/1.4/JavaCryptographicArchitecture-1.4-ruleset.zip";
    public static final boolean WARNINGS_IN_SOURCECODECLASSES_ONLY = true;

    public static final String ResourceZipPath = "/CrySLRules/CrySLRules_For_IntelliJ_1_4.zip";
    public static final String DefaultExtractLocation = "./CrySLRules/";
}
