package dev._2lstudios.mineorm.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import dev._2lstudios.mineorm.common.providers.IProvider;
import dev._2lstudios.mineorm.common.repository.Repository;
import dev._2lstudios.mineorm.common.drivers.DriverManager;
import dev._2lstudios.mineorm.common.providers.MongoDBProvider;

@SuppressWarnings("unchecked")
public class MineORMPlatform {
    private final Map<Class<?>, Repository<?>> repositories;
    private final Map<DatabaseType, IProvider> providers;
    private final Map<String, IProvider> cachedProviders;

    private final DriverManager driverManager;

    public MineORMPlatform(final File workingDirectory, final Logger logger) {
        this.repositories = new HashMap<>();
        this.providers = new HashMap<>();
        this.cachedProviders = new HashMap<>();
        this.driverManager = new DriverManager(new File(workingDirectory, "drivers"), logger);

        instance = this;
    }

    public IProvider connect(final DatabaseType type, final String connectionURI) {
        try {
            this.driverManager.downloadIfNotExistDriver(type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IProvider provider = this.cachedProviders.get(connectionURI);

        if (provider != null) {
            return provider;
        }

        switch (type) {
        case MONGODB:
            provider = new MongoDBProvider().connect(connectionURI);
            break;
        default:
            throw new Error("Unknown database type.");
        }

        providers.put(type, provider);
        cachedProviders.put(connectionURI, provider);
        return provider;
    }

    public void removeRepository(final Class<?> entity) {
        repositories.remove(entity);
    }

    public <S> Repository<?> addRepository(Class<?> entity, IProvider provider, String collection) {
        return (Repository<S>) this.repositories.put(entity, new Repository<>(entity, provider, collection));
    }

    public <S> Repository<?> addRepository(Class<?> entity, IProvider provider) {
        return (Repository<S>) this.repositories.put(entity, new Repository<>(entity, provider));
    }

    public <S> Repository<S> getRepository(Class<?> entity) {
        return (Repository<S>) this.repositories.get(entity);
    }

    /* Static instance */
    private static MineORMPlatform instance;

    public static MineORMPlatform getInstance() {
        return instance;
    }
}
