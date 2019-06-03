# CogniCrypt for Android Studio

This repository contains an Android Studio Plugin for CogniCrypt.

Requirements:

- Installation of Android Studio (tested with 2018.3.6)

# Build, Installation 

Run `gradlew assemble` in root directory

# Installation

1. Open Android Studio installation and go to `File > Settings > Plugins` and Select the gear icon and `Install Plugin from Disk...`
2. Select file `build\distributions\icognicrypt-1.0-SNAPSHOT.zip` 
3. Restart Android Studio 

# Usage

1. Build your `.apk` and make sure it successfully produces an `.apk` file within your project folder.
2. Go to to `Analyze > Run CogniCrypt`
3. The first time the analysis is launched CogniCrypt asks you to locate the CrySL rules (a popup appears). Download https://github.com/CROSSINGTUD/CryptoAnalysis/releases/download/2.3/CrySL-rulesets.zip and extract it to a folder, in the popup specify the folder you extracted the rules to.
3. Wait till analysis process terminates.