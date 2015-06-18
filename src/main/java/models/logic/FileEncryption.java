package models.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;

import org.apache.commons.codec.binary.Base64;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class FileEncryption {

    public static void main(String[] args) throws Exception {

        //Generate Symmetric key
        byte[] symmetricKey = generateSKey();
        System.out.println("key : " + symmetricKey);

        //Encrypt Data by symmetric key
        FileInputStream fis = new FileInputStream("originalFiles/original");
        FileOutputStream fos = new FileOutputStream("encrypted.txt");
        encryptData(fis, fos, symmetricKey);
        //System.out.println("Encrypted Data : " + encryptedData);

        //Generate private key public key pair
        KeyPair keyPair = generateAKeyPair();
        User user = new User("Shoeless Joe", "sjoe@gmail.com", keyPair.getPublic(), keyPair.getPrivate());

        //Encrypt symmetric key by public key
        String encryptedkey = encryptSymmetricKey(symmetricKey, user.getPublicKey());

        //Send message and key to other user having private key
        //Decrypt symmetric Key by private key
        byte[] decryptedSymmetricKey = decryptSymmetricKey(encryptedkey, user.getPrivateKey());

        //Decrypt encrypted Data by decrypted symmetric key
        FileInputStream fis2 = new FileInputStream("encrypted.txt");
        FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
        decryptData(fis2, fos2, decryptedSymmetricKey);
    

    }

    public static void encryptData(InputStream is, OutputStream os, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IOException {
        SecretKey secKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    }

    public static void decryptData(InputStream is, OutputStream os, byte[] key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secKey = new SecretKeySpec(key, "AES");

        cipher.init(Cipher.DECRYPT_MODE, secKey);
        CipherOutputStream cos = new CipherOutputStream(os, cipher);
        doCopy(is, cos);

    }

    private static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    public static byte[] generateSKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenS = KeyGenerator.getInstance("AES");
        keyGenS.init(128);
        SecretKey sKey = keyGenS.generateKey();

        return sKey.getEncoded();
    }

    public static KeyPair generateAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encryptSymmetricKey(byte[] symmetricKey, PublicKey pubKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(symmetricKey));
    }

    public static byte[] decryptSymmetricKey(String encryptedKey, PrivateKey privKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher dipher = Cipher.getInstance("RSA");
        dipher.init(Cipher.DECRYPT_MODE, privKey);
        return dipher.doFinal(Base64.decodeBase64(encryptedKey));
    }
    
    public static void keyToFile(String key, String keyPath) throws IOException {
        File file = new File(keyPath);
        char[] hex = encodeHex(key.getBytes());
        FileUtils.writeStringToFile(file, String.valueOf(hex));
    }
    
    public static String fileToKey(String keyPath) throws IOException, DecoderException {
        File keyFile = new File(keyPath);
        String data = new String(readFileToByteArray(keyFile));
        byte[] encoded;
        try {
            encoded = decodeHex(data.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
        return new String(encoded);
    }
}
