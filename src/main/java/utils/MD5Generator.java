/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author raphabot
 *
 */
public class MD5Generator {

    /**
     * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
     * 13/03/15
     *
     * @param filePath the path where the file to have the md5 generated belongs.
     * @return The MD55 in String
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String generate(String filePath) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            DigestInputStream dis = new DigestInputStream(is, md);
            /* Read stream to EOF as normal... */
        }
        byte[] digest = md.digest();
        String toReturn = "";
        for (byte b : digest) {
            toReturn = toReturn + String.valueOf(b);
        }
        return toReturn;
    }

}
