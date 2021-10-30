package dev._2lstudios.mineorm.drivers;

import java.io.File;
import java.io.IOException;

import dev._2lstudios.mineorm.DatabaseType;
import dev._2lstudios.mineorm.utils.FileUtils;
import dev._2lstudios.mineorm.utils.HTTPUtils;
import dev._2lstudios.mineorm.utils.JavaUtils;

public class Driver {

    public static String URL = "https://github.com/2lstudios-mc/mineorm/raw/master/drivers/";

    private final DatabaseType type;
    private final String name;

    private final File jarFile;
    private final File verFile;

    private String localVersion;
    private String remoteVersion;

    public Driver(final File directory, final DatabaseType type) {
        this.type = type;
        this.name = type.toString().toLowerCase();

        this.jarFile = new File(directory, name + ".jar");
        this.verFile = new File(directory, name + ".txt");
        this.jarFile.mkdirs();

        try {
            this.localVersion = FileUtils.readFirstLine(this.verFile);
        } catch (IOException e) {
            this.localVersion = "0.0.0";
            try {
                FileUtils.createAndWriteFile(this.verFile, this.localVersion);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }

        try {
            this.remoteVersion = HTTPUtils.readStringFromURL(this.getVersionURL());
        } catch (IOException e) {
            this.remoteVersion = this.localVersion;
            e.printStackTrace();
        }
    }

    public File getJarFile() {
        return this.jarFile;
    }

    public String getJarURL() {
        return URL + name + "-driver.jar";
    }

    public String getVersionURL() {
        return URL + name + "-version.txt";
    }

    public boolean exist() {
        return this.jarFile.exists();
    }

    public void download() throws IOException {
        HTTPUtils.downloadFile(this.jarFile, this.getJarURL());
        this.localVersion = this.remoteVersion;
        FileUtils.createAndWriteFile(this.verFile, this.localVersion);
    }

    public boolean isLastVersion() {
        return this.localVersion.equals(this.remoteVersion);
    }

    public String getLocalVersion() {
        return this.localVersion;
    }

    public String getRemoteVersion() {
        return this.remoteVersion;
    }

    public DatabaseType getType() {
        return this.type;
    }

    public void loadClasspath() throws IOException {
        JavaUtils.addJarFileToClassPath(this.getJarFile());
    }
}
