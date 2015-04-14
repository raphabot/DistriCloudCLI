package models.providers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import models.abstracts.ProviderAbstract;
import sun.misc.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Transient;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;
import utils.Constants;

/**
 * Created by raphabot on 21/12/14.
 */
@Entity
public class GoogleDriveProvider extends ProviderAbstract {

    /**
     * Global instance of the HTTP transport.
     */
    @Transient
    private HttpTransport httpTransport;

    /**
     * Global Drive API client.
     */
    @Transient
    private static Drive drive = null;

    /**
     * Global instance of the JSON factory.
     */
    @Transient
    private static JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    @Transient
    private GoogleAuthorizationCodeFlow flow;
    
    private String refreshToken;

    public GoogleDriveProvider() {
        super();

    }

    @Override
    public String getLoginURL() {
        String url = flow.newAuthorizationUrl().setRedirectUri(this.getRedirectURL()).build();
        return url;
    }

    @Override
    public Boolean validateToken(String code) {
        try {
            GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(this.getRedirectURL()).execute();
            GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(this.getAppID(), this.getAppSecret())
                    .build()
                    .setFromTokenResponse(response);
            //Change the code to refreshToken.
            this.setRefreshToken(credential.getRefreshToken());
            //GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

            //Create a new authorized API client
            drive = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        } catch (IOException e) {
            printStackTrace(e);
            return false;
        }

        return true;
    }

    @Override
    public String uploadFile(String filePath, String title) throws IOException {
        //Create a new authorized API client
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(this.getAppID(), this.getAppSecret())
                .setJsonFactory(jsonFactory).setTransport(this.httpTransport).build()
                .setRefreshToken(this.getRefreshToken()).setAccessToken(this.getToken());
        drive = new Drive.Builder(httpTransport, jsonFactory, credential).build();

        //Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription("A test document");
        body.setMimeType("text/plain");

        java.io.File fileContent = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("text/plain", fileContent);

        File fileg = drive.files().insert(body, mediaContent).execute();
        return fileg.getId();
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        //Create a new authorized API client
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(this.getAppID(), this.getAppSecret())
                .setJsonFactory(jsonFactory).setTransport(this.httpTransport).build()
                .setRefreshToken(this.getRefreshToken()).setAccessToken(this.getToken());
        drive = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        try {
            File file = drive.files().get(remoteFilePath).execute();
            if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
                try {
                    HttpResponse resp = drive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                    FileOutputStream outputStream;
                    outputStream = new FileOutputStream(localFilePath);
                    //Write the outputstream from provider response
                    com.google.api.client.util.IOUtils.copy(resp.getContent(), outputStream);
                } catch (IOException e) {
                    // An error occurred.
                    e.printStackTrace();
                    return false;
                }
            } else {
                // The file doesn't have any content stored on Drive.
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Long getIdProvider() {
        return this.idProvider;
    }

    @Override
    public void providerSetup() {
        this.providerType = utils.Constants.GOOGLE_PROVIDER;
        this.setAppID("779881464379-virjjj9a2i54030sj0igfirgb14amtg9.apps.googleusercontent.com");
        this.setAppSecret("-rV1gqw1mTA1GWb0J8DZmVbB");
        this.setRedirectURL("urn:ietf:wg:oauth:2.0:oob");

        /**
         * Builds an authorization flow.
         */
        flow = this.getFlow();

    }

    private GoogleAuthorizationCodeFlow getFlow() {
        if (flow == null) {
            httpTransport = new NetHttpTransport();
            jsonFactory = new JacksonFactory();

            flow
                    = new GoogleAuthorizationCodeFlow.Builder(this.httpTransport, this.jsonFactory, this.getAppID(), this.getAppSecret(), Arrays.asList(DriveScopes.DRIVE_FILE))
                    .setAccessType("offline").setApprovalPrompt("force").build();
        }
        return flow;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    

}
