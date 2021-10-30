package dev._2lstudios.mineorm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {
    public static String readFirstLine(final File file) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final String line = reader.readLine();
        reader.close();
        return line;
    }

    public static void createAndWriteFile(final File file, final String text) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        final PrintWriter writer = new PrintWriter(file, "UTF-8");
        for (final String line : text.split("\n")) {
            writer.println(line);
        }
        writer.close();
    }
}
