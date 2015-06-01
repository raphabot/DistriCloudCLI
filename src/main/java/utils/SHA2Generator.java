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
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author raphabot
 *
 */
public class SHA2Generator {

    /**
     *  Return file's SHA256.
     * @param filePath the path where the file to have the md5 generated belongs.
     * @return The SHA256 in String
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String generate(String filePath) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        String sha2 = BaseEncoding.base16().encode(Files.hash(file, Hashing.sha256()).asBytes()).toLowerCase();
        return sha2;
    }

}
