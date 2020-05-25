package com.xilidou.framework.ioc.utils;

import java.util.Optional;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Optional<Class<?>> loadClass(String className) {
        try {
            Class<?> aClass = getDefaultClassLoader().loadClass(className);
            return Optional.of(aClass);
        } catch (ClassNotFoundException ignore) {

        }

        return Optional.empty();
    }

}
