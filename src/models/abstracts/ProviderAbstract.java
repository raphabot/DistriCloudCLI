package models.abstracts;

import java.io.File;
import java.io.IOException;

/**
 * Created by raphabot on 21/12/14.
 */
public abstract class ProviderAbstract {

    /**
     * This is a unique client ID that must be required in the API website.
     */
    private static String CLIENT_ID;
    /**
     * This is a unique client secret that must be required in the API website.
     */
    private static String CLIENT_SECRET;
    /**
     * This is a unique redirect URI that must be required in the API website.
     */
    private static String REDIRECT_URI;


    /**
     * This method returns the website link that the application's user must access in order to allow access to the provider
     * @return website link
     */
    abstract public String getLoginURL();

    /**
     * This method will try to save the token in the database.
     * @param token token
     * @return true if it succeed, false otherwise
     */
    abstract public Boolean setToken(String token);

    /**
     * This method will return the token from the database.
     * @return token, if it is available. -1 otherwise
     */
    abstract public String getToken();

    /**
     * This method will upload a file to the provider.
     * @param filePath file path
     * @param title title of the file
     * @return the path or id on where to download the file
     */
    abstract public String uploadFile(String filePath, String title) throws Exception;

    /**
     * This method will download a file from the provider
     * @param localFilePath where the file will be saved
     * @param remoteFilePath file path of remote file
     * @return true if the download succeed, false otherwise
     */
    abstract public boolean downloadFile(String localFilePath, String remoteFilePath);

}
