/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.fileParts;

import javax.persistence.Entity;
import models.abstracts.FilePartAbstract;
import models.abstracts.ProviderAbstract;

/**
 *
 * @author developer
 */
@Entity
public class FilePart extends FilePartAbstract {

    public FilePart() {
        super();
    }

    public FilePart(ProviderAbstract provider, int filePart, String remotePath) {
        super(provider, filePart, remotePath);
    }
    
    
    
}
