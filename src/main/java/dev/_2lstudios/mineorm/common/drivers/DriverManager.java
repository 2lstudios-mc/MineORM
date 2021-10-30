package dev._2lstudios.mineorm.common.drivers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import dev._2lstudios.mineorm.common.DatabaseType;
import dev._2lstudios.mineorm.common.utils.JavaUtils;

public class DriverManager {

    private final File directory;
    private final Logger logger;
    private final Map<DatabaseType, Driver> drivers;

    public DriverManager(final File directory, final Logger logger) {
        this.directory = directory;
        this.logger = logger;
        this.drivers = new HashMap<>();

        this.addDriver(DatabaseType.MONGODB);

        this.checkForUpdates();
        this.loadClasspath();
    }

    public void addDriver(final DatabaseType type) {
        this.drivers.put(type, new Driver(this.directory, type));
    }

    public void checkForUpdates() {
        for (final Driver driver : this.drivers.values()) {
            try {
                if (!driver.isLastVersion()) {
                    this.logger.info("Driver for " + driver.getType() + " is outdated. Downloading from repository...");
                    driver.download();
                }

                else if (!driver.exist()) {
                    this.logger
                            .info("Driver for " + driver.getType() + " didn't exist. Downloading from repository...");
                    driver.download();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadClasspath() {
        for (final Driver driver : this.drivers.values()) {
            try {
                JavaUtils.addJarFileToClassPath(driver.getJarFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getDirectory() {
        return this.directory;
    }

}
