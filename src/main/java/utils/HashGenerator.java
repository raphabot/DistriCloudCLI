/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author raphabot
 *
 */
public class HashGenerator {

    /**
     * Return file's MD5.
     * @param filePath the path where the file to have the md5 generated belongs.
     * @return The MD55 in String
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String generateSHA512(String filePath) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        String sha512 = BaseEncoding.base16().encode(Files.hash(file, Hashing.sha512()).asBytes()).toLowerCase();
        return sha512;
    }
    
    /**
     * Return file's MD5.
     * @param filePath the path where the file to have the md5 generated belongs.
     * @return The MD55 in String
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String generateMD5(String filePath) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        String md5 = BaseEncoding.base16().encode(Files.hash(file, Hashing.md5()).asBytes()).toLowerCase();
        return md5;
    }

}
