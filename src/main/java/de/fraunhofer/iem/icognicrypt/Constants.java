package de.fraunhofer.iem.icognicrypt;

import crypto.cryslhandler.CrySLModelReader;

public class Constants {

    public final static boolean AUTOMATIC_SCAN_ON_COMPILE = false;

    public static final String DummyCrySLPath = "./CrySLRules/JCA";

    // TODO: This constant is not final in CryptoAnalysis
    public static final String CRYSL_EXTENSION = CrySLModelReader.cryslFileEnding;
    public static final String CRYSL_RULES_DOWNLOADLINK = "https://soot-build.cs.uni-paderborn.de/nexus/repository/soot-release/de/darmstadt/tu/crossing/JavaCryptographicArchitecture/1.5.1/JavaCryptographicArchitecture-1.5.1-ruleset.zip";
    public static final boolean WARNINGS_IN_SOURCECODECLASSES_ONLY = true;

    public static final String ResourceZipPath = "/CrySLRules/JCA1.5.2.zip";
    public static final String DefaultExtractLocation = "./CrySLRules/";
}
