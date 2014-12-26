package models.abstracts;

import java.io.File;

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
     * @param file file path
     * @param title title of the file
     * @return true if the upload succeed, false otherwise
     */
    abstract public boolean uploadFile(File filePath, String title);

}
