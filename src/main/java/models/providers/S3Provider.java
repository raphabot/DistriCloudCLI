/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.providers;

import models.abstracts.JCloudProviderAbstract;

/**
 *
 * @author developer
 */
public class S3Provider extends JCloudProviderAbstract{

    public S3Provider(String appId, String appSecret) {
        super();
        this.setAppID(appId);
        this.setAppSecret(appSecret);
    }
    
    
    
}
