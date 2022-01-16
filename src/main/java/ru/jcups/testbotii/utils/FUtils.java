package ru.jcups.testbotii.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class FUtils {

    public static File saveFile(String uri, String format) {
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            File file = Files.createTempFile(null, format).toFile();
            OutputStream os = new FileOutputStream(file);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
            System.out.printf("File size: %.3f Mb\n", (file.length() / (1024.0d * 1024.0d)));
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
