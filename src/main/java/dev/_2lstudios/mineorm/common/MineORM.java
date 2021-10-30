package dev._2lstudios.mineorm.common;

import java.util.HashMap;
import java.util.Map;

import dev._2lstudios.mineorm.common.providers.IProvider;
import dev._2lstudios.mineorm.common.providers.MongoProvider;
import dev._2lstudios.mineorm.common.repository.Repository;

@SuppressWarnings("unchecked")
public class MineORM {
    private static final Map<Class<?>, Repository<?>> repositories = new HashMap<>();
    private static final Map<DatabaseType, IProvider> providers = new HashMap<>();
    private static final Map<String, IProvider> cachedProviders = new HashMap<>();

    public static IProvider connect(final DatabaseType type, final String connectionURI) {
        IProvider provider = cachedProviders.get(connectionURI);

        if (provider != null) {
            return provider;
        }

        switch (type) {
        case MONGODB:
            provider = new MongoProvider().connect(connectionURI);
            break;
        default:
            throw new Error("Unknown database type.");
        }

        providers.put(type, provider);
        cachedProviders.put(connectionURI, provider);
        return provider;
    }

    public static void removeRepository(final Class<?> entity) {
        repositories.remove(entity);
    }

    public static <S> Repository<?> addRepository(Class<?> entity, IProvider provider, String collection) {
        return (Repository<S>) MineORM.repositories.put(entity, new Repository<>(entity, provider, collection));
    }

    public static <S> Repository<?> addRepository(Class<?> entity, IProvider provider) {
        return (Repository<S>) MineORM.repositories.put(entity, new Repository<>(entity, provider));
    }

    public static <S> Repository<S> getRepository(Class<?> entity) {
        return (Repository<S>) repositories.get(entity);
    }
}
