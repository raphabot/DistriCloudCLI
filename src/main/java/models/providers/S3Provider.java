/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.providers;

import javax.persistence.Entity;
import models.abstracts.JCloudProviderAbstract;

/**
 *
 * @author developer
 */
@Entity
public class S3Provider extends JCloudProviderAbstract{

    
   
    public S3Provider(){
        super();
        this.setjCloudProvider("aws-s3");
        this.setAppID("AKIAJPR4VRCIQNZ6N3WA");
        this.setAppSecret("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");
    }
    
    public S3Provider(String appId, String appSecret) {
        super(appId, appSecret, "aws-s3");
    }
    
    
    
}
