package de.fraunhofer.iem.icognicrypt.IdeSupport.platform;

import com.intellij.openapi.application.ApplicationNamesInfo;

public class IdePlatformProvider implements IIdePlatformProvider
{
    private static final String AndroidIdeName= "Android Studio";
    private static final String IntelliJIdeName = "IntelliJ IDEA";

    private final IdeType _instanceType;

    private IdePlatformProvider()
    {
        _instanceType = GetTypeInternal();
    }

    public IdeType GetRunningPlatform()
    {
        return _instanceType;
    }

    private IdeType GetTypeInternal(){
        ApplicationNamesInfo nameInfo = ApplicationNamesInfo.getInstance();
        String productName = nameInfo.getFullProductName();

        switch (productName)
        {
            case AndroidIdeName:
                return IdeType.AndroidStudio;
            case IntelliJIdeName:
                return IdeType.IntelliJ;
            default:
                return IdeType.Unknown;
        }
    }
}

