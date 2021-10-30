package dev._2lstudios.mineorm.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;

public class JavaUtils {
    public static void addJarFileToClassPath(final File file) throws IOException {
        final JarFile jarFile = new JarFile(file);
        final Enumeration<JarEntry> e = jarFile.entries();

        final URL[] urls = { new URL("jar:file:" + file + "!/") };
        final URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            final JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            try {
                cl.loadClass(className);
            } catch (final ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        jarFile.close();
    }
}
