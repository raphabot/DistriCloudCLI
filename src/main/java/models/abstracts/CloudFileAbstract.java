package models.abstracts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author developer
 */

@Entity
@Table(name="CloudFile")
public abstract class CloudFileAbstract {
    
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<FilePartAbstract> fileParts;
    
    private String name;
    
    private String md5;
    
    private String key;

    public CloudFileAbstract(String name, String md5, String key) {
        this.name = name;
        this.md5 = md5;
        this.key = key;
        this.fileParts = new ArrayList<>();
    }
    
    public CloudFileAbstract(){
        
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FilePartAbstract> getFileParts() {
        return fileParts;
    }

    public void setFileParts(List<FilePartAbstract> fileParts) {
        this.fileParts = fileParts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
    
    public void addFilePart(FilePartAbstract filePart){
        this.fileParts.add(filePart);
    }

    @Override
    public String toString() {
        return "Id: " + this.id + " Name: " + this.name + " MD5: " + this.md5 + " Key: " + this.key;
    }
    
    
}
