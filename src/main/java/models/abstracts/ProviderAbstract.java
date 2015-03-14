package models.abstracts;

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
    public ProviderAbstract(int providerType, String clientID) {
        this.providerType = providerType;
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
     * @return the path or id on where to download the file
     */
    abstract public String uploadFile(String filePath, String title) throws Exception;

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

    @Override
    public String toString() {
        return "ID: " + this.idProvider + " Provider Type: " + this.providerType + " Token: " + this.token;
    }

    

}
