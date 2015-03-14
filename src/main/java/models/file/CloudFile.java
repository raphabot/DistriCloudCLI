/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.file;

import javax.persistence.Entity;
import models.abstracts.CloudFileAbstract;

/**
 *
 * @author raphabot
 */
@Entity
public class CloudFile extends CloudFileAbstract {

    public CloudFile(String name, String md5) {
        super(name, md5);
    }
    
    public CloudFile(){
        super();
    }
    
}
