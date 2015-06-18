/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author developer
 */
public class ZipUnzip {
    
    public static void compress(String inputFile){
        compress(inputFile, 1);
    }

    public static void compress(String inputFile, int parts) {
        try {
            File inputFileH = new File(inputFile);
            String compressedFile = Constants.TEMP_FOLDER.concat(inputFileH.getName()).concat(".zip");
            ZipFile zipFile = new ZipFile(compressedFile);
            
            ZipParameters parameters = new ZipParameters();

            // COMP_DEFLATE is for compression
            // COMp_STORE no compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // DEFLATE_LEVEL_ULTRA = maximum compression
            // DEFLATE_LEVEL_MAXIMUM
            // DEFLATE_LEVEL_NORMAL = normal compression
            // DEFLATE_LEVEL_FAST
            // DEFLATE_LEVEL_FASTEST = fastest compression
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            
            // Calculate each part size
            long uncompressedSize = inputFileH.length();
            long eachPartLength = uncompressedSize / parts;
            System.out.println(uncompressedSize);
            System.out.println(eachPartLength);
            
            // file compressed
            zipFile.createZipFile(inputFileH, parameters, true, eachPartLength);

            
            File outputFileH = new File(compressedFile);
            long comrpessedSize = outputFileH.length();

            //System.out.println("Size "+uncompressedSize+" vs "+comrpessedSize);
            double ratio = (double) comrpessedSize / (double) uncompressedSize;
            //System.out.println("File compressed with compression ratio : " + ratio);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String compressedFile){
        decompress(compressedFile, 1);
    }
    
    public static void decompress(String compressedFile, int parts) {
        String destination = Constants.DOWNLOADS_FOLDER;
        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        //System.out.println("File Decompressed");
    }


    public static void main(String[] args) {

        String filePath = "originalFiles/";
        String fileName = "bike.jpg";
        String inputFile = filePath + fileName;

        long beginTime = System.nanoTime();
         compress(inputFile, 2);
        long endTime = System.nanoTime();
        System.out.println((endTime - beginTime) / 1000000);
        
        beginTime = System.nanoTime();
        //String destination = Constants.TEMP_FOLDER + "decompress";
        decompress(Constants.TEMP_FOLDER + fileName + ".zip");
        endTime = System.nanoTime();
        System.out.println((endTime - beginTime) / 1000000);

        
        
        
    }

}
