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
public abstract class JCloudProviderAbstract extends ProviderAbstract {

    private String jCloudProvider = "aws-s3";
    private String containerName = "DistriCloud";

    public JCloudProviderAbstract() {
    }

    public JCloudProviderAbstract(String appId, String appSecret) {
        super();
        this.setAppID(appId);
        this.setAppSecret(appSecret);
        providerSetup();
    }
    
    public JCloudProviderAbstract(int providerType, String clientID) {
        super(providerType, clientID);

    }

    @Override
    public void providerSetup() {
        if (this.getAppID() == null || this.getAppSecret() == null){
            return;
        }
        else{
            // Initialize the BlobStoreContext
            BlobStoreContext context = ContextBuilder.newBuilder(jCloudProvider)
                    .credentials(this.getAppID(), this.getAppSecret())
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

    @Override
    public Boolean validateToken(String token) {
        return true;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String uploadFile(String filePath, String title) throws Exception {
        
        String remotePath = "";
        // Access the BlobStore
        try (BlobStoreContext context = ContextBuilder.newBuilder(jCloudProvider)
                .credentials(this.getAppID(), this.getAppSecret())
                .buildView(BlobStoreContext.class)) {
            // Access the BlobStore
            BlobStore blobStore = context.getBlobStore();

            // Create a Blob
            File file = new File(filePath);
            ByteSourcePayload payload = new ByteSourcePayload(Files.asByteSource(file));
            Blob blob = blobStore.blobBuilder(filePath) // you can use folders via blobBuilder(folderName + "/sushi.jpg")
                    .payload(payload)
                    .build();

            // Upload the Blob
            remotePath = blobStore.putBlob(containerName, blob);
            payload.release();
            
            // Don't forget to close the context when you're done!
            context.close();
        }

        
        return remotePath;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getIdProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
