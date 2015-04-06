/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.abstracts;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.xml.ws.Service;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import static org.jclouds.blobstore.options.PutOptions.Builder.multipart;
import org.jclouds.io.payloads.ByteSourcePayload;

/**
 *
 * @author developer
 */
@Entity
public abstract class JCloudProviderAbstract extends ProviderAbstract {

    /**
     * The tag used by jClouds to identify the Provider.
     */
    private String jCloudProvider;

    /**
     * The bucket/container name in the provider.
     */
    private String containerName = "districloud";

    /**
     * The identity of the provider. It depends on the provider.
     */
    private String identity;

    /**
     * The provider's credential. It depends on the provider.
     */
    private String credential;

    public JCloudProviderAbstract() {
        providerSetup();
    }

    public JCloudProviderAbstract(String identity, String credential, String jCloudProvider) {
        this.setIdentity(identity);
        this.setCredential(credential);
        this.setjCloudProvider(jCloudProvider);
        providerSetup();
    }

    public JCloudProviderAbstract(int providerType, String clientID) {
        super(providerType, clientID);

    }

    @Override
    public void providerSetup() {
        if (this.getIdentity() == null || this.getCredential() == null || this.getjCloudProvider() == null) {
            return;
        } else {
            // Initialize the BlobStoreContext
            BlobStoreContext context = ContextBuilder.newBuilder(this.getjCloudProvider())
                    .credentials(this.getIdentity(), this.getCredential())
                    .buildView(BlobStoreContext.class);

            // Access the BlobStore
            BlobStore blobStore = context.getBlobStore();

            boolean created = blobStore.createContainerInLocation(null, containerName);
            if (created) {
                // the container didn't exist, but does now
            } else {
                // the container already existed
            }

            context.close();
        }
    }

    @Override
    public String getLoginURL() {
        return "press enter";
    }

    public String getjCloudProvider() {
        return jCloudProvider;
    }

    public void setjCloudProvider(String jCloudProvider) {
        this.jCloudProvider = jCloudProvider;
    }

    @Override
    public Boolean validateToken(String token) {
        return true;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String uploadFile(String filePath, String title) throws Exception {

        String remotePath = "";

        // Access the BlobStore
        try (BlobStoreContext context = ContextBuilder.newBuilder(jCloudProvider)
                .credentials(this.getIdentity(), this.getCredential())
                .buildView(BlobStoreContext.class)) {
            // Access the BlobStore
            BlobStore blobStore = context.getBlobStore();

            // Create a Blob
            File file = new File(filePath);
            String fileName = file.getName();

            ByteSourcePayload payload = new ByteSourcePayload(Files.asByteSource(file));
            payload.getContentMetadata().setContentLength(file.length());
            Blob blob = blobStore.blobBuilder(fileName) // you can use folders via blobBuilder(folderName + "/sushi.jpg")
                    .payload(payload)
                    .build();

            // Upload the Blob
            blobStore.putBlob(containerName, blob);
            remotePath = fileName;
            payload.release();

            // Don't forget to close the context when you're done!
            context.close();
        }

        return remotePath;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        // Access the BlobStore
        try (BlobStoreContext context = ContextBuilder.newBuilder(jCloudProvider)
                .credentials(this.getIdentity(), this.getCredential())
                .buildView(BlobStoreContext.class)) {
            // Access the BlobStore
            BlobStore blobStore = context.getBlobStore();

            // Download the Blob
            Blob blob = blobStore.getBlob(containerName, remoteFilePath);

            // Download the Blob
            InputStream openStream = blob.getPayload().openStream();
            
            // Create the local file
            File localFile = new File(localFilePath);
            
            // Write to it
            OutputStream outStream = new FileOutputStream(localFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = openStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            

            // Don't forget to close the context when you're done!
            context.close();
        } catch (IOException ex) {
            Logger.getLogger(JCloudProviderAbstract.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    @Override
    public Long getIdProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

}
