# CogniCrypt for Android Studio

This repository contains an Android Studio Plugin for CogniCrypt.

Requirements:

- Installation of Android Studio (tested with version 2018.3.6)

# Build 

Run `gradlew assemble` in root directory

# Installation

1. Open Android Studio installation and go to `File > Settings > Plugins` and Select the gear icon and `Install Plugin from Disk...`
2. Select file `build\distributions\icognicrypt-<VERSION>.zip`
3. Restart Android Studio
4. Download https://github.com/CROSSINGTUD/CryptoAnalysis/releases/download/2.3/CrySL-rulesets.zip and extract it to some folder <CRYSL-RULES>
5. Go to `File > Settings > Other Settings > CogniCrypt` and set the CrySL Rules Directory to <CRYSL-RULES>/JavaCryptographicArchitecture

# Usage

1. Build your `.apk` and make sure it successfully produces an `.apk` file within your project folder.
2. Go to `Analyze > Run CogniCrypt`
3. Wait till analysis process terminates.