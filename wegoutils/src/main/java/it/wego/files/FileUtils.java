/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Mess
 */
public class FileUtils {

    @Deprecated
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
        	is.close();
            throw new IOException("File too large");
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
        	is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    @Deprecated
    public static void writeFileFromBytes(byte[] data, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }

    public static String descriptionToFileName(String description, int lenghtMax) {
        description = description.toLowerCase();
        description = description.replaceAll("[^a-z0-9]", "_");
        description = description.replaceAll("(_)\\1+", "$1");
        description = description.substring(0, Math.min(lenghtMax, description.length()));
        return description;
    }

    public static String descriptionToFileName(String description) {
        return descriptionToFileName(description, 50);
    }
}
