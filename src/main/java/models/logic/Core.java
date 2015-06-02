package models.logic;

import DAO.Utils.SimpleEntityManager;
import models.abstracts.ProviderAbstract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import models.abstracts.CloudFileAbstract;
import models.abstracts.FilePartAbstract;
import models.file.CloudFile;
import models.file.FilePart;
import org.apache.commons.lang3.RandomStringUtils;
import services.CloudFileService;
import services.FilePartService;
import services.ProviderService;
import services.UserService;
import utils.Constants;
import utils.ZipUnzip;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    private static SimpleEntityManager simpleEntityManager;
    private static User localUser;
    
    public static void setSimpleEntityManager(SimpleEntityManager simpleEntityManager) {
        Core.simpleEntityManager = simpleEntityManager;
    }

    public static boolean encodeSplitUpload(String filePath, List<ProviderAbstract> providers) throws NoSuchAlgorithmException, IOException, Exception, Throwable {

        //Open file
        File file = new File(filePath);

        //Calculate SHA512
        String sha512 = utils.HashGenerator.generateSHA512(filePath);
        
        //Generate asymmetric Key to encrypt the fileParts
        SecretKey symmetricKey = CipherDecipher.generateKey();

        //Encrypt the symmetric key with the local user's symmetric key
        ArrayList<String> encryptedKeys = new ArrayList<>();
        encryptedKeys.add(FileEncryption.encryptSymmetricKey(symmetricKey.getEncoded(), localUser.getPublicKey()));
        
        /*
        //For each user that has access to this file, encrypt the symmetric key
        for (User user : users){
            encryptedKey.add(FileEncryption.encryptSymmetricKey(symmetricKey.getEncoded(), user.getPublicKey());
        }
        */
        
        //Create CloudFile
        String fileName = file.getName();
        CloudFile cloudFile = new CloudFile(fileName, sha512, CipherDecipher.keyToString(symmetricKey));
        
        //Zip file
        String zipedFileName = fileName.concat(".zip");
        ZipUnzip.compress(fileName, zipedFileName);
        File zipedFile = new File(zipedFileName);
        
        //Slipt file
        int numParts = providers.size();
        Splitter splitter = new Splitter(zipedFile);
        splitter.split(numParts);
        
        //Split keys
        for (String key : encryptedKeys){
            
        }

        //Iterate over each splitted file
        for (int i = 0; i < numParts; i++) {
            ProviderAbstract provider = providers.get(i);
            //provider.setup();
            String partFilePathPlain = zipedFileName + ".part." + i;
            String partFilePathCiphered = RandomStringUtils.random(15);
            
            //Compress file
            
            //Encode file
            FileInputStream fis = new FileInputStream(partFilePathPlain);
            FileOutputStream fos = new FileOutputStream(partFilePathCiphered);
            CipherDecipher.encrypt(symmetricKey, fis, fos);

            //Upload to provider
            String remotePath = provider.uploadFile(partFilePathCiphered, partFilePathCiphered);
            
            //Remove temporary files
            //new File(partFilePathCiphered).delete();
            //new File(partFilePathPlain).delete();

            //Calculate MD5
            sha512 = utils.HashGenerator.generateSHA512(partFilePathCiphered);

            //Save to db
            FilePartAbstract filePart = new FilePart(providers.get(i), i, remotePath, sha512);
            FilePartService fps = new FilePartService(simpleEntityManager);
            fps.save(filePart);

            cloudFile.addFilePart(filePart);

        }

        //Save to db
        CloudFileService cfs = new CloudFileService(simpleEntityManager);
        cfs.save(cloudFile);

        return true;
    }

    public static boolean downloadMergeDecode(CloudFileAbstract cloudFile) throws NoSuchAlgorithmException, IOException, Throwable {

        List<FilePartAbstract> fileParts = cloudFile.getFileParts();
        String fileName = cloudFile.getName();
        SecretKey key = CipherDecipher.stringToKey(cloudFile.getKey());
        
        //Download files
        int i = 0;
        for (FilePartAbstract filePart : fileParts) {
            ProviderAbstract provider = filePart.getProvider();
            String filePartNameEncrypted = fileName + ".zip.part." + i + ".encrypted";
            provider.downloadFile(filePartNameEncrypted, filePart.getRemotePath());
            

            //Check md5
            System.out.println("original: " + filePart.getMd5() + " Downloaded: " + utils.HashGenerator.generateSHA512(filePartNameEncrypted));
            if (!utils.HashGenerator.generateSHA512(filePartNameEncrypted).equals(filePart.getMd5())){
                return false;
            }
            
            //Decrypt file
            FileInputStream fis = new FileInputStream(filePartNameEncrypted);
            String filePartNameDecrypted = fileName + ".zip.part." + i;
            FileOutputStream fos = new FileOutputStream(filePartNameDecrypted);
            CipherDecipher.decrypt(key, fis , fos);
            
            //Remove temporary file on exit
            //new File(filePartNameEncrypted).deleteOnExit();
            
            i++;
        }

        //Merge files
        File file = new File(fileName.concat(".zip.part.0"));
        Splitter splitter = new Splitter(file);
        splitter.unsplit();

        //Unzip File
        ZipUnzip.decompress(fileName.concat(".zip"));
        
        //Check md5
        System.out.println("original: " + cloudFile.getMd5() + " Downloaded: " + utils.HashGenerator.generateSHA512(Constants.DOWNLOADS_FOLDER + "/" + fileName));

        //Decode file
        return true;
    }

    public static List<ProviderAbstract> listProviders() {
        ProviderService ps = new ProviderService(simpleEntityManager);
        return ps.findAll();
    }

    public static List<CloudFileAbstract> listCloudFiles() {
        CloudFileService cfs = new CloudFileService(simpleEntityManager);
        return cfs.findAll();
    }
    
    public static User createUser(String username, String email) throws NoSuchAlgorithmException{
        KeyPair kp = FileEncryption.generateAKeyPair();
        User user = new User(username, email, kp.getPublic(), kp.getPrivate());
        System.out.println(user);
        UserService us = new UserService(simpleEntityManager);
        us.save(user);
        return user;
    }
    
    public static User loadUser(long id){
        UserService us = new UserService(simpleEntityManager);
        return us.getById(id);
    }
    
    public static boolean isFirstTime(){
        UserService us = new UserService(simpleEntityManager);
        List<User> users = us.findAll();
        if (users.isEmpty()){
            return true;
        }
        return false;
    }

    public static User getLocalUser() {
        return localUser;
    }
    
    public static boolean setLocalUser(){
        UserService us = new UserService(simpleEntityManager);
        List<User> users = us.findAll();
        localUser = users.get(0);
        return true;
    }

}
