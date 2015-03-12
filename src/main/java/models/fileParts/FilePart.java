/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.fileParts;

import models.abstracts.FilePartAbstract;
import models.abstracts.ProviderAbstract;

/**
 *
 * @author developer
 */
public class FilePart extends FilePartAbstract {

    public FilePart() {
        super();
    }

    public FilePart(Long id, ProviderAbstract provider, int filePart, String remotePath) {
        super(id, provider, filePart, remotePath);
    }
    
    
    
}
