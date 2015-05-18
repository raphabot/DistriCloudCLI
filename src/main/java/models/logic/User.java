/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.logic;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import utils.Constants;

/**
 *
 * @author raphabot
 */
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    
    @Lob
    private byte[] publicKey;
    
    @Lob
    private byte[] privateKey;

    public User(){
        
    }
    
    public User(String username, String email, PublicKey publicKey, PrivateKey privateKey) {
        this.username = username;
        this.email = email;
        this.publicKey = publicKey.getEncoded();
        this.privateKey = privateKey.getEncoded();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public PublicKey getPublicKey() {
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(Constants.SYMMETRIC_ALGORITHM);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(this.publicKey);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public PrivateKey getPrivateKey() {
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(Constants.SYMMETRIC_ALGORITHM);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(this.privateKey);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", publicKey=" + publicKey + ", privateKey=" + privateKey + '}';
    }

    
}
