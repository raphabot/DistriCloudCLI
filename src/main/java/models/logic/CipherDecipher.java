/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 *
 * @author Raphael Bottino Based on:
 * http://www.avajava.com/tutorials/lessons/how-do-i-encrypt-and-decrypt-files-using-des.html
 */
public class CipherDecipher {

    public static void main(String[] args) {
        try {
            //String key = "squirrel123"; // needs to be at least 8 characters for DES
            System.out.println(utils.HashGenerator.generateSHA512("originalFiles/original"));

            FileInputStream fis = new FileInputStream("originalFiles/original");
            FileOutputStream fos = new FileOutputStream("encrypted.txt");
            SecretKey key = generateKey();
            keyToFile(key, "key");
            encrypt(key, fis, fos);
            System.out.println(utils.HashGenerator.generateSHA512("encrypted.txt"));

            String keyString = keyToString(key);
            System.out.println(keyString);
            System.out.println(key);
            key = null;
            
            key = stringToKey(keyString);
            
            FileInputStream fis2 = new FileInputStream("encrypted.txt");
            FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
            decrypt(key, fis2, fos2);
            System.out.println(utils.HashGenerator.generateSHA512("decrypted.txt"));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenS = KeyGenerator.getInstance("AES");
        keyGenS.init(128);
        SecretKey sKey = keyGenS.generateKey();

        return sKey;
    }

    public static void keyToFile(SecretKey key, String keyPath) throws IOException {
        File file = new File(keyPath);
        char[] hex = encodeHex(key.getEncoded());
        FileUtils.writeStringToFile(file, String.valueOf(hex));
    }

    public static SecretKey fileToKey(String keyPath) throws IOException, DecoderException {
        File keyFile = new File(keyPath);
        String data = new String(readFileToByteArray(keyFile));
        byte[] encoded;
        try {
            encoded = decodeHex(data.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
        return new SecretKeySpec(encoded, "AES");
    }
    
    public static String keyToString(SecretKey key){
        char[] hex = encodeHex(key.getEncoded());
        return String.valueOf(hex);
    }
    
    public static SecretKey stringToKey(String keyString){
        byte[] encoded;
        try {
            encoded = decodeHex(keyString.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
        return new SecretKeySpec(encoded, "AES");
    }

    public static void encrypt(SecretKey key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(SecretKey key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(SecretKey key, int mode, InputStream is, OutputStream os) throws Throwable {
       Cipher cipher = Cipher.getInstance("AES");
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

}
