package models.logic;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class FileEncryption {

    public static void main(String[] args) throws Exception {

        //Generate Symmetric key
        byte[] symmetricKey = generateSKey();
        System.out.println("key : " + symmetricKey);

        //Encrypt Data by symmetric key
        String encryptedData = encryptData("My Secured Message", symmetricKey);
        System.out.println("Encrypted Data : " + encryptedData);

        //Generate private key public key pair
        KeyPair keyPair = generateAKeyPair();
        User user = new User("Shoeless Joe", "sjoe@gmail.com", keyPair.getPublic(), keyPair.getPrivate());
        
        //Encrypt symmetric key by public key
        String encryptedkey = encryptSymmetricKey(symmetricKey, user.getPublicKey());
        

        //Send message and key to other user having private key
        
        
        //Decrypt symmetric Key by private key
        byte[] decryptedSymmetricKey = decryptSymmetricKey(encryptedkey, user.getPrivateKey());

        //Decrypt encrypted Data by decrypted symmetric key
        System.out.println("Decrypted Data : " + decryptData(encryptedData, decryptedSymmetricKey));

    }

    public static String encryptData(String data, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKey secKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] newData = cipher.doFinal(data.getBytes());

        return Base64.encodeBase64String(newData);
    }

    public static String decryptData(String inputData, byte[] key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secKey = new SecretKeySpec(key, "AES");

        cipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] newData = cipher.doFinal(Base64.decodeBase64(inputData.getBytes()));
        return new String(newData);

    }
    
    public static byte[] generateSKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenS = KeyGenerator.getInstance("AES");
        keyGenS.init(128);
        SecretKey sKey = keyGenS.generateKey();

        return sKey.getEncoded();
    }
    
    public static KeyPair generateAKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encryptSymmetricKey(byte[] symmetricKey, PublicKey pubKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(symmetricKey));
    }
    
    public static byte[] decryptSymmetricKey(String encryptedKey, PrivateKey privKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher dipher = Cipher.getInstance("RSA");
        dipher.init(Cipher.DECRYPT_MODE, privKey);
        return dipher.doFinal(Base64.decodeBase64(encryptedKey));
    }
}
