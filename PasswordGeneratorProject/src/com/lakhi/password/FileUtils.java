package com.lakhi.password;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtils {

    public static void saveToTextFile(String filePath, List<String> passwords) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        for (String password : passwords) {
            writer.write(password + System.lineSeparator());
        }
        writer.close();
    }

    public static void saveToCSVFile(String filePath, List<String> passwords) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write("Password\n");
        for (String password : passwords) {
            writer.write(password + "\n");
        }
        writer.close();
    }
}