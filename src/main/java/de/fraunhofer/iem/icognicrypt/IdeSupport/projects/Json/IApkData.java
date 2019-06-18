package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json;

public interface IApkData
{
    void SetOutputFile(String relativeFilePath);
    void SetBaseName(String baseName);
    void SetFullName(String fullName);
    void SetOutputType(ApkDataOutputType outputType);
    void SetVersionCode(int versionCode);
    void SetVersionName(String versionName);

    String GetOutputFile();
    String GetBaseName();
    String GetFullName();
    ApkDataOutputType GetOutputType();
    int GetVersionCode();
    String GetVersionName();
}
