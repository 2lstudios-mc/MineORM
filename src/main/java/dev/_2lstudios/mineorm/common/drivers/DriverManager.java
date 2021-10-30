package dev._2lstudios.mineorm.common.drivers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import dev._2lstudios.mineorm.common.DatabaseType;

public class DriverManager {

    private final File directory;
    private final Logger logger;
    private final Map<DatabaseType, Driver> drivers;

    public DriverManager(final File directory, final Logger logger) {
        this.directory = directory;
        this.logger = logger;
        this.drivers = new HashMap<>();
    }

    public void downloadIfNotExistDriver(final DatabaseType type) throws IOException {
        if (!drivers.containsKey(type)) {
            final Driver driver = this.drivers.put(type, new Driver(this.directory, type));
            if (!driver.isLastVersion()) {
                this.logger.info("Driver for " + driver.getType() + " is outdated. Downloading from repository...");
                driver.download();
            } else if (!driver.exist()) {
                this.logger.info("Driver for " + driver.getType() + " didn't exist. Downloading from repository...");
                driver.download();
            }
        }
    }
}
