package models.abstracts;

import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import utils.Constants;

/**
 * Created by raphabot on 21/12/14.
 */
@Entity
@Table(name = "Provider")
public abstract class ProviderAbstract {

    /**
     * This is a unique provider ID to database use.
     */
    @Id
    @GeneratedValue
    protected Long idProvider;

    /**
     * This integer represents the type of provider the class represents.
     */
    public int providerType;

    /**
     * This String represents a account name given by the user just to identify the account.
     */
    private String accountName;
    
    /**
     * This is a unique client ID that must be required in the API website.
     */
    @Transient
    private String appID;

    /**
     * This is a unique client secret that must be required in the API website.
     */
    @Transient
    private String appSecret;

    /**
     * This is a unique redirect URI that must be required in the API website.
     */
    @Transient
    private String redirectURL;

    private String token;

    /**
     * Default ProviderAbstract Constructor
     *
     * @param providerType
     * @param clientID
     * @param clientSecret
     * @param redirectURL
     */
    public ProviderAbstract(int providerType, String accountName) {
        this.providerType = providerType;
        this.accountName = accountName;
        providerSetup();
    }

    public ProviderAbstract() {
        providerSetup();

    }
    
    abstract public void providerSetup();

    public int getProviderType() {
        return providerType;
    }

    public String getAppID() {
        return appID;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIdProvider(Long idProvider) {
        this.idProvider = idProvider;
    }

    public void setProviderType(int providerType) {
        this.providerType = providerType;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    

    /**
     * This method returns the website link that the application's user must
     * access in order to allow access to the provider
     *
     * @return website link
     */
    abstract public String getLoginURL();

    /**
     * This method will validate the token with the provider.
     *
     * @param token token
     * @return true if it succeed, false otherwise
     */
    abstract public Boolean validateToken(String token);

    /**
     * This method will return the token from the database.
     *
     * @return token, if it is available. -1 otherwise
     */
    public String getToken() {
        if (this.token.isEmpty()) {
            return "-1";
        }
        return this.token;
    }

    /**
     * This method will upload a file to the provider.
     *
     * @param filePath file path
     * @param title title of the file
     * @param parent parent folder id/path. Null if uploading to root.
     * @return the path or id on where to download the file
     */
    abstract public String uploadFile(String filePath, String title, String parent) throws Exception;

    /**
     * This method will download a file from the provider
     *
     * @param localFilePath where the file will be saved
     * @param remoteFilePath file path of remote file
     * @return true if the download succeed, false otherwise
     */
    abstract public boolean downloadFile(String localFilePath, String remoteFilePath);

    /**
     * This method will return the provider Id
     *
     * @return provider Id
     */
    abstract public Long getIdProvider();
    
    /**
     * This method will create a folder and return it's Id/path
     *
     * @param folderName Folder name
     * @param parentFolder  Parent folder id/path
     * @return folder id/path
     */
    abstract public String createFolder(String folderName, String parentFolder);
    
    /**
     * This method will list all items inside a given folder
     *
     * @param folderName Folder path to be listed
     * @return Hashmap Where the key is the fileName and the value is the path/id
     */
    abstract public HashMap<String, String> listItems(String folderPath);
    
    /**
     * This method will download a part from all keys from a given file
     *
     * @param localFolder The local folder to save parts to
     * @param fileName The file name
     */
    abstract public void downloadKeysPart(String localFolder, String fileName);
    
    /**
     * This method will check if a item is inside a folder
     *
     * @param folderName Folder name
     * @param parentFolder  Parent folder name
     * @return return id/path if inside, null otherwise
     */
    public String isInside(String folderName, String parentFolder){
        HashMap<String, String> items = listItems(parentFolder);
        if (items.containsKey(folderName)){
            return items.get(folderName);
        }
        return null;
    }
    

    @Override
    public String toString() {
        return "ID: " + this.idProvider + " Provider Type: " + this.providerType + " Token: " + this.token;
    }

    

}
