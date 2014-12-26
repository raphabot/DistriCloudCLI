package models.providers;

import models.abstracts.ProviderAbstract;

import java.io.File;
import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;

/**
 * Created by raphabot on 22/12/14.
 */
public class DropboxProvider extends ProviderAbstract {

    /**
     * This is a unique client ID that must be required in the API website.
     */
    private static final String APP_KEY = "zyulddisxwf26u5";
    /**
     * This is a unique client secret that must be required in the API website.
     */
    private static final String APP_SECRET = "gz0hgjw5ulo853o";
    /**
     * This is a unique redirect URI that must be required in the API website.
     */
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    public DropboxProvider() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

    }

    @Override
    public String getLoginURL() {
        return null;
    }

    @Override
    public Boolean setToken(String token) {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean uploadFile(File filePath, String title) {
        return false;
    }
}
