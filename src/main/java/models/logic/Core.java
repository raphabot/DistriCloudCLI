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
import models.abstracts.CloudFileAbstract;
import models.abstracts.FilePartAbstract;
import models.file.CloudFile;
import models.file.FilePart;
import services.CloudFileService;
import services.FilePartService;
import services.ProviderService;
import services.UserService;
import utils.Constants;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    private static SimpleEntityManager simpleEntityManager;
    private static User localUser;

    public static void setSimpleEntityManager(SimpleEntityManager simpleEntityManager) {
        Core.simpleEntityManager = simpleEntityManager;
    }

    public static boolean encodeSplitUpload(String filePath, List<ProviderAbstract> providers) throws Exception, IOException, Throwable {
        ArrayList<User> users = new ArrayList<>();
        users.add(localUser);
        return encodeSplitUpload(filePath, providers, users);
    }

    public static boolean encodeSplitUpload(String filePath, List<ProviderAbstract> providers, List<User> users) throws NoSuchAlgorithmException, IOException, Exception, Throwable {

        //Open file
        File file = new File(filePath);
        if (file.exists() == false) {
            return false;
        }
        String fileName = file.getName();

        //Seting temp folder
        String tempFolder = "tmp/".concat(fileName.trim()).concat("/");

        //Calculate SHA512
        String sha512 = utils.HashGenerator.generateSHA512(filePath);

        //Generate symmetric Key to encrypt the fileParts
        //SecretKey symmetricKey = CipherDecipher.generateKey();
        byte[] symmetricKey = FileEncryption.generateSKey();

        //Encrypt the symmetric key with the local user's public asymmetric key
        ArrayList<String> encryptedKeys = new ArrayList<>();
        //encryptedKeys.add(FileEncryption.encryptSymmetricKey(symmetricKey.getEncoded(), localUser.getPublicKey()));

        //Add the user himself to the users list
        //users.add(localUser);
        //For each user that has access to this file, encrypt the symmetric key
        for (User user : users) {
            String keyFilePath = tempFolder.concat("keys/").concat(user.getEmail()).concat(".key");
            String encryptedSymmetricKey = FileEncryption.encryptSymmetricKey(symmetricKey, user.getPublicKey());
            FileEncryption.keyToFile(encryptedSymmetricKey, keyFilePath);
            encryptedKeys.add(encryptedSymmetricKey);

            //Erasure keys
            File keyFile = new File(keyFilePath);
            Splitter splitter = new Splitter(keyFile);
            splitter.split(providers.size(), tempFolder.concat("keys/").concat(user.getEmail()).concat("/"));

        }

        //Create CloudFile - Metadata
        CloudFile cloudFile = new CloudFile(CloudFile.UPLOADING, fileName, sha512, localUser);

        //Zip file
        //String zipedFileName = Constants.TEMP_FOLDER + fileName.concat(".zip");
        //ZipUnzip.compress(filePath);
        //File zipedFile = new File(zipedFileName);
        
        //Encode file
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(tempFolder.concat(fileName));
        //CipherDecipher.encrypt(symmetricKey, fis, fos);
        FileEncryption.encryptData(fis, fos, symmetricKey);
        
        File encodedFile = new File(tempFolder.concat(fileName));
        int numParts = providers.size();
        //Erasure file
        Splitter splitter = new Splitter(encodedFile);
        splitter.split(numParts, tempFolder);
        splitter = null;

        //Iterate over each file part
        for (int i = 0; i < numParts; i++) {
            ProviderAbstract provider = providers.get(i);

            //Create file folder
            String remoteFolder = provider.createFolder(fileName.trim(), null);
            //File already exists remotely
            if (remoteFolder == null){
                return false;
            }

            //Upload file to provider
            String partFilePath = tempFolder.concat(fileName.concat(".part." + i));
            String fileRemotePath = provider.uploadFile(partFilePath, fileName.concat(".part." + i), remoteFolder);

            //Keys
            remoteFolder = provider.createFolder("keys", fileName.trim());
            for (User user : users) {
                String keyPartFilePath = tempFolder.concat("keys/").concat(user.getEmail()).concat("/").concat(user.getEmail()).concat(".key").concat(".part." + i);
                String keyRemotePath = provider.uploadFile(keyPartFilePath, user.getEmail().concat(".key").concat(".part." + i), remoteFolder);
            }

            //Remove temporary files
            //new File(partFilePathCiphered).delete();
            //new File(partFilePathPlain).delete();
            //Calculate Hash
            sha512 = utils.HashGenerator.generateSHA512(partFilePath);

            //Save to db
            FilePartAbstract filePart = new FilePart(providers.get(i), i, fileRemotePath, sha512);
            FilePartService fps = new FilePartService(simpleEntityManager);
            fps.save(filePart);

            cloudFile.addFilePart(filePart);

        }

        //Save to db
        cloudFile.setStatus(CloudFile.OK);
        CloudFileService cfs = new CloudFileService(simpleEntityManager);
        cfs.save(cloudFile);

        return true;
    }

    public static boolean downloadMergeDecode(CloudFileAbstract cloudFile) throws NoSuchAlgorithmException, IOException, Throwable {

        List<FilePartAbstract> fileParts = cloudFile.getFileParts();
        String fileName = cloudFile.getName();
        // = null;//FileEncryption.decryptSymmetricKey(encryptedSymmetricKey, localUser.getPrivateKey());
        //SecretKey key = CipherDecipher.stringToKey(cloudFile.getKey());

        //Download all
        int i = 0;
        String downloadFolderPath = Constants.DOWNLOADS_FOLDER.concat("/").concat(fileName).concat("/tmp/");
        for (FilePartAbstract filePart : fileParts) {
            ProviderAbstract provider = filePart.getProvider();

            // Download all KeysPart
            String keyDownloadFolderPath = downloadFolderPath.concat("keys");
            File keyDownloadFolder = new File(keyDownloadFolderPath);
            keyDownloadFolder.mkdirs();
            provider.downloadKeysPart(keyDownloadFolderPath, fileName);

            String filePartName = downloadFolderPath.concat(fileName) + ".part." + i;
            provider.downloadFile(filePartName, filePart.getRemotePath());

            //Check hash
            System.out.println("original: " + filePart.getMd5() + " Downloaded: " + utils.HashGenerator.generateSHA512(filePartName));
            if (!utils.HashGenerator.generateSHA512(filePartName).equals(filePart.getMd5())) {
                return false;
            }

            i++;
        }

        //Merge keys
        File keyPart = new File(downloadFolderPath.concat("/keys/").concat(localUser.getEmail()).concat(".key.part.0"));
        Splitter splitter = new Splitter(keyPart);
        splitter.unsplit();
        
        //Merge files
        File file = new File(downloadFolderPath.concat(fileName).concat(".part.0"));
        splitter = new Splitter(file);
        splitter.unsplit();

        //Decrypt symmetricKey
        String encryptedSymmetricKey = FileEncryption.fileToKey(downloadFolderPath.concat("keys/").concat(localUser.getEmail()).concat(".key"));
        byte[] symmetricKey = FileEncryption.decryptSymmetricKey(encryptedSymmetricKey, localUser.getPrivateKey());
        
        //Decrypt file
        FileInputStream fis = new FileInputStream(downloadFolderPath.concat(fileName));
        String filePartNameDecrypted = Constants.DOWNLOADS_FOLDER.concat("/").concat(fileName).concat("/").concat(fileName);
        FileOutputStream fos = new FileOutputStream(filePartNameDecrypted);
        //CipherDecipher.decrypt(key, fis , fos);
        FileEncryption.decryptData(fis, fos, symmetricKey);

            //Remove temporary file on exit
        //new File(filePartNameEncrypted).deleteOnExit();
        //Unzip File
        //ZipUnzip.decompress(fileName.concat(".zip"));
        //Check hash
        System.out.println("original: " + cloudFile.getHash() + " Downloaded: " + utils.HashGenerator.generateSHA512(filePartNameDecrypted));

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

    public static User createUser(String username, String email) throws NoSuchAlgorithmException {
        KeyPair kp = FileEncryption.generateAKeyPair();
        User user = new User(username, email, kp.getPublic(), kp.getPrivate());
        System.out.println(user);
        UserService us = new UserService(simpleEntityManager);
        us.save(user);
        return user;
    }

    public static User loadUser(long id) {
        UserService us = new UserService(simpleEntityManager);
        return us.getById(id);
    }

    public static boolean isFirstTime() {
        UserService us = new UserService(simpleEntityManager);
        List<User> users = us.findAll();
        if (users.isEmpty()) {
            return true;
        }
        return false;
    }

    public static User getLocalUser() {
        return localUser;
    }

    public static boolean setLocalUser() {
        UserService us = new UserService(simpleEntityManager);
        List<User> users = us.findAll();
        localUser = users.get(0);
        return true;
    }

}
