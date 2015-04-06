/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.providers;

import javax.persistence.Entity;
import models.abstracts.JCloudProviderAbstract;
import utils.Constants;

/**
 *
 * @author developer
 */
@Entity
public class MSAzureProvider extends JCloudProviderAbstract{

    
   
    public MSAzureProvider(){
        super();
        this.setjCloudProvider(Constants.JCLOUD_MS_AZURE);
        
    }
    
    public MSAzureProvider(String identity, String credential) {
        super(identity, credential, Constants.JCLOUD_MS_AZURE);
    }
    
    
    
}
