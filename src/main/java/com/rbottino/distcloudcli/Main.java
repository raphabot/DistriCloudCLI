/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbottino.distcloudcli;

import DAO.Utils.SimpleEntityManager;
import models.abstracts.ProviderAbstract;
import models.logic.Core;
import models.providers.DropboxProvider;
import models.providers.GoogleDriveProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;
import models.abstracts.CloudFileAbstract;
import models.file.CloudFile;
import models.file.FilePart;
import models.providers.MSAzureProvider;
import models.providers.S3Provider;
import services.CloudFileService;
import services.FilePartService;
import services.ProviderService;
import utils.Constants;

public class Main {

    public static void main(String[] args) {

        int code = -1;

        while (code == -1) {

            System.out.println("Choose an option:");
            System.out.println("1 - List providers");
            System.out.println("2 - Add provider");
            System.out.println("3 - Delete a provider");
            System.out.println("4 - List files");
            System.out.println("5 - Uplaod file");
            System.out.println("6 - Download file");
            //System.out.println("7 - Delete file");
            System.out.println("0 - Exit");

            SimpleEntityManager simpleEntityManager = new SimpleEntityManager(Constants.PERSISTENCE_UNIT_NAME);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                code = Integer.parseInt(br.readLine());
                switch (code) {
                    case 1:
                    {
                        ProviderService ps = new ProviderService(simpleEntityManager);
                        List<ProviderAbstract> providers = ps.findAll();
                        for (ProviderAbstract provider : providers) {
                            System.out.println(provider);
                        }
                        break;
                    }    
                    case 2:
                    {
                        System.out.println("Choose the provider type. '" + utils.Constants.GOOGLE_PROVIDER + "' for GoogleDrive and '" + utils.Constants.DROPBOX_PROVIDER + "' for Dropbox");
                        code = Integer.parseInt(br.readLine());
                        ProviderAbstract provider = null;
                        switch (code){
                            case utils.Constants.GOOGLE_PROVIDER:
                            {
                                provider = new GoogleDriveProvider();
                                String url = provider.getLoginURL();
                                System.out.println("Enter the following link in your browser and paste the token here:");
                                System.out.println(url);
                                String token = br.readLine();
                                provider.setToken(token);
                                provider.validateToken(token);
                                break;
                            }
                            
                            case utils.Constants.DROPBOX_PROVIDER:
                            {
                                provider = new DropboxProvider();
                                String url = provider.getLoginURL();
                                System.out.println("Enter the following link in your browser and paste the token here:");
                                System.out.println(url);
                                String token = br.readLine();
                                provider.setToken(token);
                                provider.validateToken(token);
                                break;
                            }
                            
                            case utils.Constants.S3_PROVIDER:
                            {
                                System.out.println("Enter the AccessKey:");
                                String acessKey = br.readLine();
                                System.out.println("Enter the SecretKey:");
                                String secretKey = br.readLine();
                                provider = new S3Provider(acessKey, secretKey);
                                break;
                            }
                            
                            case utils.Constants.MSAZURE_PROVIDER:
                            {
                                System.out.println("Enter the AccessKey:");
                                String acessKey = br.readLine();
                                System.out.println("Enter the SecretKey:");
                                String secretKey = br.readLine();
                                provider = new MSAzureProvider(acessKey, secretKey);
                                break;
                            }
                            
                            default:
                            {
                                System.out.println("No provider available");
                                break;
                            }
                        }
                        
                        ProviderService ps = new ProviderService(simpleEntityManager);
                        ps.save(provider);
                        
                        break;
                    }
                    
                    case 3:
                    {
                        System.out.println("Enter the id to delete:");
                        code = Integer.parseInt(br.readLine());
                        
                        ProviderService ps = new ProviderService(simpleEntityManager);
                        ps.delete(code);
                        
                        break;
                    }
                        
                    case 4:
                    {
                        CloudFileService cfs = new CloudFileService(simpleEntityManager);
                        List<CloudFileAbstract> cloudFiles = cfs.findAll();
                        
                        for (CloudFileAbstract cloudFile : cloudFiles){
                            System.out.println(cloudFile);
                        }
                        
                        break;
                    }
                    
                    case 5: 
                    {
                        System.out.println("Enter the absolut file path:");
                        String filePath = br.readLine();
                        ProviderService ps = new ProviderService(simpleEntityManager);
                        List<ProviderAbstract> providers = ps.findAll();

                        try {
                            Core.encodeSplitUpload(filePath, providers);
                        } catch (Exception e) {
                            printStackTrace(e);
                        }

                        break;
                    }
                    
                    case 6: 
                    {
                        System.out.println("Enter the file id:");
                        code = Integer.parseInt(br.readLine());
                        CloudFileService cfs = new CloudFileService(simpleEntityManager);
                        CloudFileAbstract cloudFile = cfs.getById(code);
                        try {
                            Core.downloadMergeDecode(cloudFile);
                        } catch (Exception e){
                            printStackTrace(e);
                        }
                        break;
                    }
                    
                    case 0:
                    {
                        simpleEntityManager.close();
                        break;
                    }
                    default:
                        break;
                }
                
                
            } 
            catch (Exception e) {
                printStackTrace(e);
            }

            if (code != 0){
                code = -1;
            }
            
        }
        
 


        /*
         SimpleEntityManager simpleEntityManager = new SimpleEntityManager(Constants.PERSISTENCE_UNIT_NAME);
        
         DropboxProvider dbp = new DropboxProvider(Constants.DROPBOX_PROVIDER, null);
         dbp.setToken("test");
         ProviderService ps = new ProviderService(simpleEntityManager);
         ps.save(dbp);
        
         FilePart fp = new FilePart(dbp, 0, "remote_path", "123");
         FilePartService fps = new FilePartService(simpleEntityManager);
         fps.save(fp);
        
         //SimpleEntityManager simpleEntityManager = new SimpleEntityManager(Constants.PERSISTENCE_UNIT_NAME);
         for (ProviderAbstract pa : ps.findAll()){
         System.out.println(pa.getIdProvider());
         }
        
        
         dbp.setToken("test");

         EntityManagerFactory factory = Persistence.createEntityManagerFactory("hsqldb");
         EntityManager manager = factory.createEntityManager();

         ps.save(dbp);

        
        
         System.out.println("Provider/'s ID: " + dbp.getIdProvider());

        
        
         ArrayList<ProviderAbstract> providers = new ArrayList<ProviderAbstract>();
         GoogleDriveProvider drive = new GoogleDriveProvider(Constants.GOOGLE_PROVIDER, null);
         providers.add(drive);
         DropboxProvider dropbox = new DropboxProvider(Constants.DROPBOX_PROVIDER, null);
         providers.add(dropbox);
         for (int i = 0; i < providers.size(); i++){
         ProviderAbstract provider = providers.get(i);
         System.out.println(provider.getLoginURL());
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         try{
         String code = br.readLine();
         System.out.println("token: " + code);
         System.out.println("setToken: " + provider.setToken(code));
         //System.out.println("uploadFile: " + provider.uploadFile("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt", "Teste 2.txt"));
         }catch (Exception e){}
         }

         ArrayList <String> remoteFilePaths = Core.encodeSplitUpload("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt", providers);
         Core.downloadMergeDecode("/home/raphabot/IdeaProjects/DistiCloudCLI/resultado-download-merge.txt", remoteFilePaths, providers);
        
         */
        /*
         try {
         for (int i = 0; i < providers.size(); i++){
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         String id = br.readLine();
         providers.get(i).downloadFile("download.part."+i,id);
         }
         } catch (Exception e) {
         e.printStackTrace();
         }
         */
        /*
         GoogleDriveProvider drive = new GoogleDriveProvider();
         System.out.println(drive.getLoginURL());
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         try{
         String code = br.readLine();
         System.out.println("token: " + code);
         System.out.println("setToken: " + drive.setToken(code));
         System.out.println("uploadFile: " + drive.uploadFile(new File("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt"), "Teste 2"));
         }catch (Exception e){}
         DropboxProvider dropbox = new DropboxProvider();
         System.out.println(dropbox.getLoginURL());
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         try{
         String code = br.readLine();
         System.out.println("token: " + code);
         System.out.println("setToken: " + dropbox.setToken(code));
         System.out.println("uploadFile: " + dropbox.uploadFile(new File("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt"), "Teste 2.txt"));
         }catch (Exception e){}
         */
        //providers.add(drive);
    }

    
}

/*
 bd:
 n contas (user, codigo, tipo)
 n arquivos (nome)
 cada arquivo tem n partes(posicao, id_cloud)
 cada parte esta associada a uma conta
 */
