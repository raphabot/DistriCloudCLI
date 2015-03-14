package models.logic;

import models.abstracts.ProviderAbstract;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    public static ArrayList <String> encodeSplitUpload(String filePath, ArrayList<ProviderAbstract> providers) throws NoSuchAlgorithmException, IOException{

        //Encode file
        
        
        //Calculate MD5
        String md5 = utils.MD5Generator.generate(filePath);

        //Save to db
        

        //Slipt file
        ArrayList <String> remoteFilePaths = new ArrayList <String>();
        int numParts = providers.size();
        
        File file = new File(filePath);
        Splitter splitter = new Splitter(file);
        splitter.split(numParts);

        //Upload files
        for (int i = 0; i < numParts; i++){
            ProviderAbstract provider = providers.get(i);
            try{
                remoteFilePaths.add(provider.uploadFile(filePath + ".part." + i, "part" + i));
                
                //Calculate MD5
                
                
                //Sabe to db
                
                
            }catch (Exception e){return null;}
        }
        return remoteFilePaths;
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
