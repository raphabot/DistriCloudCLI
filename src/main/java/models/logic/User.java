/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.logic;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author raphabot
 */
@Entity
@Table(name="User")
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    private String email;
    private PublicKey publicKey; 
    private PrivateKey privateKey; 

    
    public User(String username, String email, PublicKey publicKey, PrivateKey privateKey) {
        this.username = username;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    
    
    
    
    
}
