package models.providers;

import java.io.File;
import com.dropbox.core.*;
import models.abstracts.ProviderAbstract;

import java.io.*;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.Transient;
import utils.Constants;

/**
 * Created by raphabot on 22/12/14.
 */
@Entity
public class DropboxProvider extends ProviderAbstract {

  
    @Transient
    private DbxWebAuthNoRedirect webAuth;
    @Transient
    private DbxRequestConfig config;
    @Transient
    private DbxClient client;

    
    public DropboxProvider(int providerType, String clientID) {
        super(providerType, clientID);
        DbxAppInfo appInfo = new DbxAppInfo(this.getAppID(), this.getAppSecret());
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
    public String uploadFile(String filePath, String title) throws Exception {
        File inputFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(inputFile);
        DbxEntry.File uploadedFile = client.uploadFile("/"+title, DbxWriteMode.add(), inputFile.length(), inputStream);
        System.out.println("Uploaded: " + uploadedFile.path.toString());
        inputStream.close();
        return uploadedFile.path.toString();
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

    @Override
    public Long getIdProvider() {
        return this.idProvider;
    }
}

