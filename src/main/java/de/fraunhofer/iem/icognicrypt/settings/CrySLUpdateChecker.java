package de.fraunhofer.iem.icognicrypt.settings;

import de.fraunhofer.iem.icognicrypt.Constants;
import com.intellij.openapi.application.PathManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import de.fraunhofer.iem.icognicrypt.ui.NotificationProvider;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CrySLUpdateChecker extends CogniCryptSettings implements StartupActivity {
    private static final Logger LOG = Logger.getInstance(CrySLUpdateChecker.class);
    private static final String GROUP_DISPLAY_ID = "Error";
    public static String latest_version;
    public static String current_version;

    private ICogniCryptSettings _currentState;

    @Override
    public void runActivity(@NotNull Project project) {
            _currentState = ServiceManager.getService(IPersistableCogniCryptSettings.class);
            current_version = _currentState.getCurrentVersion();
            try {
                latest_version = getLatestVersion();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (latest_version_Available()) {
                Notifications.Bus.notify(
                        new Notification(
                                GROUP_DISPLAY_ID,
                                "CogniCrypt Message: CrySL Rule Update",
                                 "A Newer version of CrySL ruleset available."+"\n" +"<html><a href=''>Update</a> now.",
                                NotificationType.INFORMATION,
                                (notification, hyperlinkEvent) -> {
                                    try {
                                        update(current_version, latest_version);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }),
                        project);
            }
            else{
            }
        }

    public boolean latest_version_Available() {
        String[] l_version = latest_version.split(Pattern.quote("."));
        String[] c_version = current_version.split(Pattern.quote("."));
        int i = 0;
        while (i < l_version.length) {
            int l_int = Integer.parseInt(l_version[i]);
            int c_int = Integer.parseInt(c_version[i]);
            if (l_int > c_int) {
                return true;
            } else if (l_int == c_int) {
                i++;
            } else {
                return false;
            }
        }
        return false;
    }

    public void update(String cur_version, String lat_version) throws IOException {
        String sourceURL = "https://soot-build.cs.uni-paderborn.de/nexus/repository/soot-release/de/darmstadt/tu/crossing/JavaCryptographicArchitecture/1.5.2/JavaCryptographicArchitecture-1.5.2-ruleset.zip";

        String executingjarpath = PathManager.getJarPathForClass(CrySLUpdateChecker.class);
        Path jarPath = Paths.get(executingjarpath);
        Path pluginsDirectory = jarPath.getParent().toAbsolutePath();
        String resourcePath = Paths.get(pluginsDirectory.toString()).toFile().getCanonicalPath();

        URL url = new URL(sourceURL);
        String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
        Path targetPath = new File(resourcePath + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        deleteOldCrySL(resourcePath);
        current_version = latest_version;
        _currentState.setCurrentVersion(latest_version);
        NotificationProvider.ShowInfo("Crysl rules updated to " + _currentState.getCurrentVersion());
        extractDownloadedCrySL(targetPath.toString(), fileName, resourcePath);
    }

    private void extractDownloadedCrySL(String targetPath, String fileName, String resourcePath) throws IOException {
        File downloadedFile = new File(targetPath);
        File target = new File(resourcePath + File.separator + "CrySLRules" + File.separator);
        if (!downloadedFile.exists()) {
            throw new IOException(downloadedFile.getAbsolutePath() + " does not exist");
        } else {
            InputStream inputStream = new FileInputStream(downloadedFile.getAbsoluteFile());
            final ZipInputStream zipInputStreamStream = new ZipInputStream(inputStream);
            ZipEntry nextEntry;
            while ((nextEntry = zipInputStreamStream.getNextEntry()) != null) {
                final String name = nextEntry.getName();
                if (!name.endsWith("/")) {
                    final File nextFile = new File(target, name);
                    final File parent = nextFile.getParentFile();
                    if (parent != null) {
                        parent.mkdirs();
                    }
                    try (OutputStream targetStream = new FileOutputStream(nextFile)) {
                        copy(zipInputStreamStream, targetStream);
                    }
                }
            }
        }
    }

    public static void copy(final InputStream source, final OutputStream target) throws IOException {
        final int bufferSize = 4 * 1024;
        final byte[] buffer = new byte[bufferSize];

        int nextCount;
        while ((nextCount = source.read(buffer)) >= 0) {
            target.write(buffer, 0, nextCount);
        }
    }

    private void deleteOldCrySL(String resourcePath) {
        String oldCrySLPath = resourcePath + File.separator + "CrySLRules" + File.separator + "JavaCryptographicArchitecture" + File.separator;
        File index = new File(oldCrySLPath);
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            currentFile.delete();
        }
        index.delete();
    }


    public String getLatestVersion() throws IOException {
        URL obj = new URL("https://soot-build.cs.uni-paderborn.de/nexus/repository/soot-release/de/darmstadt/tu/crossing/JavaCryptographicArchitecture/maven-metadata.xml");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Document doc = (Document) Jsoup.parse(response.toString(), "", Parser.xmlParser());
            Element e = doc.getElementsByTag("latest").first();
            latest_version = e.text();
        }
        return latest_version;
    }


    public String getCurrentVersions() throws IOException {

        String executingjarpath = PathManager.getJarPathForClass(CrySLUpdateChecker.class);
        Path jarPath = Paths.get(executingjarpath);
        Path pluginsDirectory = jarPath.getParent().toAbsolutePath();
        String resourcePath = Paths.get(pluginsDirectory.toString()).toFile().getCanonicalPath();

        File dir = new File(resourcePath);
        List<String> allZipCrySLRules = searchForFileNameContainingSubstring(dir, "JavaCryptographicArchitecture");
        String cVersion = CompareVersion(allZipCrySLRules);
        _currentState.setCurrentVersion(cVersion);
        try {
            return cVersion;
        } catch (Exception e) {
            return Constants.CrySL_Version;
        }

    }

    private String CompareVersion(List<String> allZipCrySLRules) {
        for (String fileName : allZipCrySLRules) {
            fileName = fileName.replaceAll("[^\\d.]", "");
            return fileName;
        }
        return null;
    }

    private List<String> searchForFileNameContainingSubstring(File file, String substring) {

        List<String> filesContainingSubstring = new ArrayList<String>();

        if (file.exists() && file.isDirectory()) {
            String[] files = file.list(); //get the files in String format.
            for (String fileName : files) {
                if (fileName.contains(substring))
                    filesContainingSubstring.add(fileName);
            }
        }
        return filesContainingSubstring;
    }

}
