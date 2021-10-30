package dev._2lstudios.mineorm.common;

import dev._2lstudios.mineorm.common.providers.IProvider;
import dev._2lstudios.mineorm.common.repository.Repository;

public class MineORM {
    public static IProvider connect(final DatabaseType type, final String connectionURI) {
        return MineORMPlatform.getInstance().connect(type, connectionURI);
    }

    public static void removeRepository(final Class<?> entity) {
        MineORMPlatform.getInstance().removeRepository(entity);
    }

    public static <S> Repository<?> addRepository(Class<?> entity, IProvider provider, String collection) {
        return MineORMPlatform.getInstance().addRepository(entity, provider, collection);
    }

    public static <S> Repository<?> addRepository(Class<?> entity, IProvider provider) {
        return MineORMPlatform.getInstance().addRepository(entity, provider);
    }

    public static <S> Repository<S> getRepository(Class<?> entity) {
        return MineORMPlatform.getInstance().getRepository(entity);
    }
}