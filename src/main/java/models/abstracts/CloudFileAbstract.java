package models.abstracts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import models.logic.User;

/**
 *
 * @author developer
 */

@Entity
@Table(name="CloudFile")
public abstract class CloudFileAbstract {
    
    public static final int UPLOADING = 0;
    public static final int OK = 1;
    public static final int DELETED = 2;
    
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<FilePartAbstract> fileParts;
    
    private int status;
    private String name;
    private String hash;
    
    @OneToOne
    private User owner;

    public CloudFileAbstract(int status, String name, String hash, User owner) {
        this.status = status;
        this.name = name;
        this.hash = hash;
        this.fileParts = new ArrayList<>();
        this.owner = owner;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String md5) {
        this.hash = md5;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public void addFilePart(FilePartAbstract filePart){
        this.fileParts.add(filePart);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    @Override
    public String toString() {
        return "Id: " + this.id + " Status: " + this.status + " Owner: " + this.owner + " Name: " + this.name + " MD5: " + this.hash;
    }
    
    
}
