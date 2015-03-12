package models.abstracts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author developer
 */

@Entity
@Table(name="FilePart")
public abstract class FilePartAbstract {
    
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private ProviderAbstract provider;
    
    private int filePart;
    
    private String remotePath;
    
    
    public FilePartAbstract() {
        
    }
    
    public FilePartAbstract(ProviderAbstract provider, int filePart, String remotePath) {
        this.provider = provider;
        this.filePart = filePart;
        this.remotePath = remotePath;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProviderAbstract getProvider() {
        return provider;
    }

    public void setProvider(ProviderAbstract provider) {
        this.provider = provider;
    }

    public int getFilePart() {
        return filePart;
    }

    public void setFilePart(int filePart) {
        this.filePart = filePart;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
    
    
    
}
