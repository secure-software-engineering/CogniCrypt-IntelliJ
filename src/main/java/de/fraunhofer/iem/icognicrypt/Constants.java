package de.fraunhofer.iem.icognicrypt;

import crypto.cryslhandler.CrySLModelReader;

public class Constants {

    public final static boolean AUTOMATIC_SCAN_ON_COMPILE = false;

    public static final String DummyCrySLPath = "./CrySLRules/JavaCryptographicArchitecture";
    public static final String CrySL_Version = "1.5.1";
    // TODO: This constant is not final in CryptoAnalysis
    public static final String CRYSL_EXTENSION = CrySLModelReader.cryslFileEnding;
    public static final String CRYSL_RULES_DOWNLOADLINK = "https://soot-build.cs.uni-paderborn.de/nexus/repository/soot-release/de/darmstadt/tu/crossing/JavaCryptographicArchitecture/1.5.2/JavaCryptographicArchitecture-1.5.2-ruleset.zip";
    public static final boolean WARNINGS_IN_SOURCECODECLASSES_ONLY = true;

    public static final String ResourceZipPath = "JavaCryptographicArchitecture-1.5.1-ruleset.zip";
    public static final String DefaultExtractLocation = "./CrySLRules/";
}
