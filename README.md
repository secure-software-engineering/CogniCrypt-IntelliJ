# CogniCrypt for Android Studio

This repository contains an Android Studio Plugin for CogniCrypt.

**Launch Requirements**:
- Android Studio (tested with version 2018.3.6)
  
---

# Build 

##### Build Requirements
- IntelliJ  2019.1.3
- Java Development Kit 8 (either Oracle or OpenJDK will do)

Using JDK 8 currently is a hard requirement. Any version lower or higher will cause a build error. There are two places to make sure the project is built with a specific JDK version: 

1. Open the module settings for ***icognicrypt*** (F4) and check if the Project SDK under `Project Settings -> Project` refers to a JDK 8 version.
2. Gradle settings:
    a) Gradle utilized in the IDE's terminal gets its JDK reference through the `JAVA_HOME` environment variable of the operating system. Change the value of that variable if required and reboot the system for the changes to take effect. You can check the used JDK/JVM by typing `gradlew -v`.
    b) When using the IDE's built-in run and debug commands (the buttons in the top toolbar), Gradle uses a JDK version that is specified in the settings of IntelliJ. Open the settings and go to `Build, Execution, Deployment -> Build Tools -> Gradle`. Edit the `Gradle JVM` setting as required (suggestion: choose the option: *Use Project JDK*).
   
Run `gradlew assemble`  or `gradlew build` in root directory.

---

# Debug

To enable the debug functionality you need to add a run configuration in IntelliJ. 
1. Click `Add Configuration` in the top toolbar.
2. Press the `+` symbol and add a Gradle build.
3. As Gradle Project choose ***icognicrypt*** from the drop down menu.
4. Enter `:runIde` as Task and optionally `--info` as Argument.
5. Press **Apply** and **OK** 

---

# Installation

1. Open Android Studio installation and go to `File > Settings > Plugins` and Select the gear icon and `Install Plugin from Disk...`
2. Select file `build\distributions\icognicrypt-<VERSION>.zip`
3. Restart Android Studio
4. Download https://github.com/CROSSINGTUD/CryptoAnalysis/releases/download/2.3/CrySL-rulesets.zip and extract it to some folder <CRYSL-RULES>
5. Go to `File > Settings > Other Settings > CogniCrypt` and set the CrySL Rules Directory to <CRYSL-RULES>/JavaCryptographicArchitecture

---

# Usage

1. Build your `.apk` and make sure it successfully produces an `.apk` file within your project folder.
2. Go to `Analyze > Run CogniCrypt`
3. Wait till analysis process terminates.
