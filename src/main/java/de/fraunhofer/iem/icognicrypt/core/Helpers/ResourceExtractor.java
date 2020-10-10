package de.fraunhofer.iem.icognicrypt.core.Helpers;

import com.intellij.openapi.application.PathManager;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLExtractor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ResourceExtractor
{
    protected final String ExecutingJarPath;

    protected ResourceExtractor()
    {
        ExecutingJarPath = PathManager.getJarPathForClass(CrySLExtractor.class);
    }

    protected abstract boolean CheckExtractRequired();

    public abstract void Extract();

    public final void ExtractIfRequired()
    {
        if (CheckExtractRequired())
            Extract();
    }


    // (c) Christoph Dähne https://gist.github.com/christoph-daehne/e7ecf4abf26da41072b31e0431d841e7#file-ziputils-java
    protected final void unzip(String resourcePath, File target) throws IOException
    {
        Path jarPath = Paths.get(ExecutingJarPath);
        Path pluginsDirectory = jarPath.getParent().toAbsolutePath();
        resourcePath= Paths.get(pluginsDirectory.toString(), Constants.ResourceZipPath).toFile().getCanonicalPath();
        InputStream inputStream = new FileInputStream(resourcePath);
        final ZipInputStream zipStream = new ZipInputStream(inputStream);
        ZipEntry nextEntry;
        while ((nextEntry = zipStream.getNextEntry()) != null)
        {
            final String name = nextEntry.getName();
            // only extract files
            if (!name.endsWith("/"))
            {
                final File nextFile = new File(target, name);

                // create directories
                final File parent = nextFile.getParentFile();
                if (parent != null)
                {
                    parent.mkdirs();
                }

                // write file
                try (OutputStream targetStream = new FileOutputStream(nextFile))
                {
                    copy(zipStream, targetStream);
                }
            }
        }
    }


    // (c) Christoph Dähne https://gist.github.com/christoph-daehne/e7ecf4abf26da41072b31e0431d841e7#file-ziputils-java
    private static void copy(final InputStream source, final OutputStream target) throws IOException
    {
        final int bufferSize = 4 * 1024;
        final byte[] buffer = new byte[bufferSize];

        int nextCount;
        while ((nextCount = source.read(buffer)) >= 0)
        {
            target.write(buffer, 0, nextCount);
        }
    }
}
