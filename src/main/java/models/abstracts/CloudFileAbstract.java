package models.abstracts;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import sun.security.provider.MD5;

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
    private ArrayList<FilePartAbstract> fileParts;
    
    private String name;
    
    private String md5;

    public CloudFileAbstract(String name, String md5) {
        this.name = name;
        this.md5 = md5;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<FilePartAbstract> getFileParts() {
        return fileParts;
    }

    public void setFileParts(ArrayList<FilePartAbstract> fileParts) {
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

    public void addFilePart(FilePartAbstract filePart){
        this.fileParts.add(filePart);
    }
    
    
}
