package models.abstracts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import sun.security.provider.MD5;

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
    
    private String md5;
    
    
    public FilePartAbstract() {
        
    }
    
    public FilePartAbstract(ProviderAbstract provider, int filePart, String remotePath, String md5) {
        this.provider = provider;
        this.filePart = filePart;
        this.remotePath = remotePath;
        this.md5 = md5;
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
