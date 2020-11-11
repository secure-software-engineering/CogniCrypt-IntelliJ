package de.fraunhofer.iem.icognicrypt.core.android;

import com.android.tools.idea.sdk.IdeSdks;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AndroidPlatformLocator
{
    private static final Logger logger = Logger.getInstance(AndroidPlatformLocator.class);

    private static final String AndroidSkd = "ANDROID_SDK";

    public static Path getAndroidPlatformsLocation(Project project) {
        File androidSdkPath = IdeSdks.getInstance().getAndroidSdkPath();
        String android_sdk_root;

        if (androidSdkPath != null) {
            android_sdk_root = androidSdkPath.getAbsolutePath();
            logger.info("Choosing android sdk path automatically");
        }
        else {
            android_sdk_root = System.getenv(AndroidSkd);
            logger.info("Fallback for android sdk path to environment variable");
        }

        Path sdkPath = Paths.get(android_sdk_root);
        if (android_sdk_root == null || android_sdk_root.equals("") || !sdkPath.toFile().exists())
            throw new RuntimeException("Environment variable " + AndroidSkd + " not found!");
        return sdkPath.resolve("platforms");
    }
}
