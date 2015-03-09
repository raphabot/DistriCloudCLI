package models.providers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
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

/**
 * Created by raphabot on 21/12/14.
 */
public class GoogleDriveProvider extends ProviderAbstract {

    /**
     * This is a unique client ID that must be required in the API website.
     */
    private static final String CLIENT_ID = "779881464379-virjjj9a2i54030sj0igfirgb14amtg9.apps.googleusercontent.com";
    /**
     * This is a unique client secret that must be required in the API website.
     */
    private static final String CLIENT_SECRET = "-rV1gqw1mTA1GWb0J8DZmVbB";
    /**
     * This is a unique redirect URI that must be required in the API website.
     */
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    /**
     * Global instance of the HTTP transport.
     */
    private HttpTransport httpTransport = new NetHttpTransport();

    /**
     * Global Drive API client.
     */
    private static Drive drive = null;

    /**
     * Global instance of the JSON factory.
     */
    private static JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    /** */
    private String token;

    private GoogleAuthorizationCodeFlow flow;

    public GoogleDriveProvider() {
        /** Builds an authorization flow.*/
        flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE_FILE)).setAccessType("online").setApprovalPrompt("auto").build();


    }

    @Override
    public String getLoginURL() {
        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        return url;
    }

    @Override
    public Boolean setToken(String token) {
        this.token = token;
        try {
            GoogleTokenResponse response = flow.newTokenRequest(token).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

            //Create a new authorized API client
            drive = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public String getToken() {
        if (this.token.isEmpty()) {
            return "-1";
        }
        return this.token;
    }

    @Override
    public String uploadFile(String filePath, String title) throws IOException {

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
        //the filePath, in google's provider, is an id.
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
}
