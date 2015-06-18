package models.providers;

import java.io.File;
import com.dropbox.core.*;
import models.abstracts.ProviderAbstract;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public DropboxProvider() {
        super();

        this.setAppID("zyulddisxwf26u5");
        this.setAppSecret("gz0hgjw5ulo853o");
        this.setRedirectURL("urn:ietf:wg:oauth:2.0:oob");

        DbxAppInfo appInfo = new DbxAppInfo(this.getAppID(), this.getAppSecret());
        config = new DbxRequestConfig("DistriCloud/0.1", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    }

    @Override
    public String getLoginURL() {
        return webAuth.start();
    }

    @Override
    public Boolean validateToken(String token) {

        try {
            // This will fail if the user enters an invalid authorization code.
            DbxAuthFinish authFinish = webAuth.finish(token);
            String accessToken = authFinish.accessToken;
            this.setToken(accessToken);
            client = new DbxClient(config, accessToken);
        } catch (DbxException e) {
            return false;
        }

        return true;
    }

    @Override
    public String uploadFile(String filePath, String title, String folder) throws Exception {
        if (this.client == null) {
            client = new DbxClient(config, this.getToken());
        }
        File inputFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(inputFile);
        if (folder != null && !folder.isEmpty()) {
            title = folder.concat("/").concat(title);
        }
        DbxEntry.File uploadedFile = client.uploadFile("/" + title, DbxWriteMode.add(), inputFile.length(), inputStream);
        System.out.println("Uploaded: " + uploadedFile.path.toString());
        inputStream.close();
        return uploadedFile.path.toString();
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        if (this.client == null) {
            client = new DbxClient(config, this.getToken());
        }
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
        } finally {
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

    @Override
    public void providerSetup() {
        this.setAppID("zyulddisxwf26u5");
        this.setAppSecret("gz0hgjw5ulo853o");
        this.setRedirectURL("urn:ietf:wg:oauth:2.0:oob");

        DbxAppInfo appInfo = new DbxAppInfo(this.getAppID(), this.getAppSecret());
        config = new DbxRequestConfig("DistriCloud/0.1", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    }

    @Override
    public String createFolder(String folderName, String parentFolder) {
        if (this.client == null) {
            client = new DbxClient(config, this.getToken());
        }

        try {
            if (parentFolder != null && !parentFolder.isEmpty()) {
                folderName = parentFolder.concat("/").concat(folderName);
            }
            DbxEntry.Folder folder = client.createFolder("/".concat(folderName));
            if (folder == null){
                return null;
            }
            return folder.path;
        } catch (DbxException ex) {
            Logger.getLogger(DropboxProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public HashMap<String, String> listItems(String folderPath) {
        HashMap<String, String> items = new HashMap<>();
        if (this.client == null) {
            client = new DbxClient(config, this.getToken());
        }

        DbxEntry.WithChildren listing;
        try {
            listing = client.getMetadataWithChildren(folderPath);
            for (DbxEntry child : listing.children) {
                items.put(child.name, child.path);
            }
        } catch (DbxException ex) {
            Logger.getLogger(DropboxProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    @Override
    public void downloadKeysPart(String localFolder, String fileName) {
        HashMap<String, String> items = listItems("/".concat(fileName).concat("/keys"));
        for (Map.Entry<String, String> entry : items.entrySet()){
            downloadFile(localFolder.concat("/").concat(entry.getKey()), entry.getValue());
        }
        
    }

    
}
