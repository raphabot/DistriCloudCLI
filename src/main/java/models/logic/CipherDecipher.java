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
import static org.apache.commons.codec.binary.Hex.encodeHex;

/**
 *
 * @author Raphael Bottino Based on:
 * http://www.avajava.com/tutorials/lessons/how-do-i-encrypt-and-decrypt-files-using-des.html
 */
public class CipherDecipher {

    public static void main(String[] args) {
        try {
            //String key = "squirrel123"; // needs to be at least 8 characters for DES
            System.out.println(utils.MD5Generator.generate("originalFiles/original"));
            
            FileInputStream fis = new FileInputStream("originalFiles/original");
            FileOutputStream fos = new FileOutputStream("encrypted.txt");
            SecretKey key = generateKey();
            encrypt(key, fis, fos);
            System.out.println(utils.MD5Generator.generate("encrypted.txt"));
            
            FileInputStream fis2 = new FileInputStream("encrypted.txt");
            FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
            decrypt(key, fis2, fos2);
            System.out.println(utils.MD5Generator.generate("decrypted.txt"));
            
            
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
    
    public static boolean keyToFile(SecretKey key, String keyPath){
        File file = new File(keyPath);
        char[] hex = encodeHex(key.getEncoded());
        FileUtils.writeStringToFile(file, String.valueOf(hex));
        return false;
    }

    public static void encrypt(SecretKey key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(SecretKey key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(SecretKey key, int mode, InputStream is, OutputStream os) throws Throwable {
        /*
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("AES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("AES"); // DES/ECB/PKCS5Padding for SunJCE
        */
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
