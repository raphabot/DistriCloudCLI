/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.file;

import javax.persistence.Entity;
import models.abstracts.CloudFileAbstract;
import models.logic.User;

/**
 *
 * @author raphabot
 */
@Entity
public class CloudFile extends CloudFileAbstract {

    public CloudFile(int status, String name, String hash, User owner) {
        super(status, name, hash, owner);
    }
    
    public CloudFile(){
        super();
    }
    
}
