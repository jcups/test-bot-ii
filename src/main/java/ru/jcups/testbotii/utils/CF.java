package ru.jcups.testbotii.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class CF {

    public static final String path = "src/main/resources/static/codes.txt";

    public static String[] get(String code) {
        List<String> result = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path, Charset.defaultCharset()))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.startsWith(code)) {
                    result.add(line.substring(8));
                } else {
                    if (result.size() >= 1)
                        break;
                }
            }
            return result.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
