package models.logic;

import DAO.Utils.SimpleEntityManager;
import models.abstracts.ProviderAbstract;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import models.abstracts.FilePartAbstract;
import models.file.CloudFile;
import models.file.FilePart;
import services.CloudFileService;
import services.FilePartService;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    private static SimpleEntityManager simpleEntityManager;
    
    public static boolean encodeSplitUpload(String filePath, ArrayList<ProviderAbstract> providers) throws NoSuchAlgorithmException, IOException{

        //Open file
        File file = new File(filePath);
        
        //Encode file
        
        
        //Calculate MD5
        String md5 = utils.MD5Generator.generate(filePath);

        //Save to db
        CloudFile cloudFile = new CloudFile(file.getName(), md5);
        CloudFileService cfs = new CloudFileService(simpleEntityManager);
        cfs.save(cloudFile);

        //Slipt file
        int numParts = providers.size();
        Splitter splitter = new Splitter(file);
        splitter.split(numParts);

        //Iterate over each splitted file
        for (int i = 0; i < numParts; i++){
            ProviderAbstract provider = providers.get(i);
            try{
                String partFilePath = filePath + ".part." + i;
                
                //Upload to provider
                String remotePath = provider.uploadFile(partFilePath, "part" + i);
                
                //Calculate MD5
                md5 = utils.MD5Generator.generate(partFilePath);
                
                //Save to db
                FilePartAbstract filePart = new FilePart(providers.get(i), i, remotePath, md5);
                FilePartService fps = new FilePartService(simpleEntityManager);
                fps.save(filePart);
                
                
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }

    public static boolean downloadMergeDecode(String filePath, ArrayList <String> remoteFilePaths, ArrayList<ProviderAbstract> providers){

        int numParts = providers.size();

        //Download files
        for (int i = 0; i < numParts; i++){
            ProviderAbstract provider = providers.get(i);
            try{
                System.out.println("downloadFile: " + provider.downloadFile(filePath + ".part." + i, remoteFilePaths.get(i)));
            }catch (Exception e){return false;}
        }

        //Merge files
        File file = new File(filePath + ".part.0");
        Splitter splitter = new Splitter(file);
        splitter.unsplit();

        //Decode file


        return false;
    }
}
