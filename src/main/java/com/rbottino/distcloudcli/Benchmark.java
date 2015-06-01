/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbottino.distcloudcli;

import java.io.File;
import java.util.ArrayList;
import models.logic.Splitter;
import utils.ZipUnzip;

/**
 * This class is all about benchmarking.
 * @author developer
 */
public class Benchmark {
    
    /**
     * This test consist in compressing then splitting the file.
     * @param filePath the path of the original file
     * @param numParts the number of parts that the compressed file must be splitted.
     * @return the time expended in milleseconds. 
     */
    private static long compressSplit(String filePath, int numParts){
        long beginTime = System.nanoTime();
        String compressedPath = filePath.concat(".compressed");
        ZipUnzip.compress(filePath, compressedPath);
        
        File compressedFile = new File(compressedPath);
        
        Splitter splitter = new Splitter(compressedFile);
        splitter.split(numParts);
        
        return ((System.nanoTime() - beginTime) /1000000 );
    }
    
    
    /**
     * This test consist in splitting then compressing the file.
     * @param filePath the path of the original file
     * @param numParts the number of parts that the original file must be splitted.
     * @return the time expended in milleseconds. 
     */
    private static long splitCompress(String filePath, int numParts){
        long beginTime = System.nanoTime();
        
        File originalFile = new File(filePath);
        
        Splitter splitter = new Splitter(originalFile);
        splitter.split(numParts);
        
        for (int i =0; i < numParts; i++){
            String partPath = filePath.concat(".part." + i);
            ZipUnzip.compress( partPath, partPath.concat(".compressed"));
        }
        
        return ((System.nanoTime() - beginTime) /1000000 );
    }
    
    public static void main(String[] args){
      
        ArrayList<String> filePaths = new ArrayList<>();
        //filePaths.add("originalFiles/original");
        filePaths.add("originalFiles/512mbfile");
        filePaths.add("originalFiles/1gbfile");
        
        int numParts = 5;
        
        for (String filePath : filePaths){
            System.out.print(compressSplit(filePath, numParts));
            System.out.print(" ");
            System.out.print(splitCompress(filePath, numParts));
            System.out.print("\n");
        }
        
    }
    
}
