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
public class MSAzureProvider extends JCloudProviderAbstract{

    
   
    public MSAzureProvider(){
        super();
        this.setjCloudProvider("azureblob");
        this.setAppID("raphabot");
        this.setAppSecret("wBLjsZHAk/XRoNSHdYY5ljvQn3eS33n4VVHcdimdebimfiQqto2pr0HvmdiG0OFUyO+rZBe7yF2UPD/xa8GUuA==");
    }
    
    public MSAzureProvider(String appId, String appSecret) {
        super(appId, appSecret, "azureblob");
    }
    
    
    
}
