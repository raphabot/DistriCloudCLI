package models.logic;

import models.abstracts.ProviderAbstract;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    public static boolean encodeSplitUpload(String filePath, ArrayList<ProviderAbstract> providers){

        int numParts = providers.size();

        //Encode file


        //Slipt file
        File file = new File(filePath);
        Splitter splitter = new Splitter(file);
        splitter.split(numParts);

        for (int i = 0; i < numParts; i++){
            ProviderAbstract provider = providers.get(i);
            try{
                System.out.println("uploadFile: " + provider.uploadFile(filePath + ".part." + i, "part" + i));
            }catch (Exception e){}
        }
        return false;
    }
}
