package models.logic;

import DAO.Utils.SimpleEntityManager;
import models.abstracts.ProviderAbstract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
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

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    private static SimpleEntityManager simpleEntityManager;

    public static void setSimpleEntityManager(SimpleEntityManager simpleEntityManager) {
        Core.simpleEntityManager = simpleEntityManager;
    }

    public static boolean encodeSplitUpload(String filePath, List<ProviderAbstract> providers) throws NoSuchAlgorithmException, IOException, Exception, Throwable {

        //Open file
        File file = new File(filePath);

        //Calculate MD5
        String md5 = utils.HashGenerator.generateSHA512(filePath);
        
        //Generate a Key to encrypt the fileParts
        SecretKey key = CipherDecipher.generateKey();

        //Create CloudFile
        String fileName = file.getName();
        CloudFile cloudFile = new CloudFile(fileName, md5, CipherDecipher.keyToString(key));
        
        //Slipt file
        int numParts = providers.size();
        Splitter splitter = new Splitter(file);
        splitter.split(numParts);

        //Iterate over each splitted file
        for (int i = 0; i < numParts; i++) {
            ProviderAbstract provider = providers.get(i);
            //provider.setup();
            String partFilePathPlain = filePath + ".part." + i;
            String partFilePathCiphered = RandomStringUtils.random(15);
            
            //Encode file
            FileInputStream fis = new FileInputStream(partFilePathPlain);
            FileOutputStream fos = new FileOutputStream(partFilePathCiphered);
            CipherDecipher.encrypt(key, fis, fos);

            //Upload to provider
            String remotePath = provider.uploadFile(partFilePathCiphered, partFilePathCiphered);
            
            //Remove temporary files
            //new File(partFilePathCiphered).delete();
            //new File(partFilePathPlain).delete();

            //Calculate MD5
            md5 = utils.HashGenerator.generateSHA512(partFilePathCiphered);

            //Save to db
            FilePartAbstract filePart = new FilePart(providers.get(i), i, remotePath, md5);
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
            String filePartNameEncrypted = fileName + ".part." + i + ".encrypted";
            provider.downloadFile(filePartNameEncrypted, filePart.getRemotePath());
            

            //Check md5
            System.out.println("original: " + filePart.getMd5() + " Downloaded: " + utils.HashGenerator.generateSHA512(filePartNameEncrypted));
            if (!utils.HashGenerator.generateSHA512(filePartNameEncrypted).equals(filePart.getMd5())){
                return false;
            }
            
            //Decrypt file
            FileInputStream fis = new FileInputStream(filePartNameEncrypted);
            String filePartNameDecrypted = fileName + ".part." + i;
            FileOutputStream fos = new FileOutputStream(filePartNameDecrypted);
            CipherDecipher.decrypt(key, fis , fos);
            
            //Remove temporary file on exit
            //new File(filePartNameEncrypted).deleteOnExit();
            
            i++;
        }

        //Merge files
        File file = new File(fileName + ".part.0");
        Splitter splitter = new Splitter(file);
        splitter.unsplit();

        //Check md5
        System.out.println("original: " + cloudFile.getMd5() + " Downloaded: " + utils.HashGenerator.generateSHA512(fileName));

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
        UserService us = new UserService(simpleEntityManager);
        List<User> users = us.findAll();
        return users.get(0);
    }

}
