package models.providers;

import java.io.File;
import com.dropbox.core.*;
import models.abstracts.ProviderAbstract;

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

    private String token;

    private DbxWebAuthNoRedirect webAuth;
    private DbxRequestConfig config;
    private DbxClient client;

    public DropboxProvider() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        config = new DbxRequestConfig("DistriCloud/0.1",Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    }

    @Override
    public String getLoginURL() {
        return webAuth.start();
    }

    @Override
    public Boolean setToken(String token) {
        try{
            // This will fail if the user enters an invalid authorization code.
            DbxAuthFinish authFinish = webAuth.finish(token);
            String accessToken = authFinish.accessToken;

            client = new DbxClient(config, accessToken);
        }catch (DbxException e){return false;}

        return true;
    }

    @Override
    public String getToken() {
        if (this.token.isEmpty()){
            return "-1";
        }
        return this.token;
    }

    @Override
    public boolean uploadFile(String filePath, String title) {
        try {
            File inputFile = new File(filePath);
            FileInputStream inputStream = new FileInputStream(inputFile);

            DbxEntry.File uploadedFile = client.uploadFile("/"+title, DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
            inputStream.close();
        }
        catch (Exception e){return false;}
        return true;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(localFilePath);
            DbxEntry.File downloadedFile = client.getFile(remoteFilePath, null, outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        finally {
            try {
                //file successfully written and closed
                outputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}

