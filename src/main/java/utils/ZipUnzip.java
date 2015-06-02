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

    public static void compress(String inputFile, String compressedFile) {
        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            File inputFileH = new File(inputFile);
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

            // file compressed
            zipFile.addFile(inputFileH, parameters);

            long uncompressedSize = inputFileH.length();
            File outputFileH = new File(compressedFile);
            long comrpessedSize = outputFileH.length();

            //System.out.println("Size "+uncompressedSize+" vs "+comrpessedSize);
            double ratio = (double) comrpessedSize / (double) uncompressedSize;
            //System.out.println("File compressed with compression ratio : " + ratio);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String compressedFile) {
        String destination = Constants.DOWNLOADS_FOLDER;
        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            if (zipFile.isEncrypted()) {
                //zipFile.setPassword(password);
            }
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        //System.out.println("File Decompressed");
    }


    public static void main(String[] args) {

        String filePath = "originalFiles/";
        String fileName = "original";
        String inputFile = filePath + fileName;

        long beginTime = System.nanoTime();
        String compressedFile = inputFile + "_" + beginTime + ".zip";
        compress(inputFile, compressedFile);
        long endTime = System.nanoTime();
        System.out.println((endTime - beginTime) / 1000000);
        
        beginTime = System.nanoTime();
        String destination = filePath + "decompress";
        decompress(compressedFile);
        endTime = System.nanoTime();
        System.out.println((endTime - beginTime) / 1000000);

        
        
        
    }

}
