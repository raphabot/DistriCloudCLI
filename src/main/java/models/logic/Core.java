package models.logic;

import DAO.Utils.SimpleEntityManager;
import models.abstracts.ProviderAbstract;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import models.abstracts.CloudFileAbstract;
import models.abstracts.FilePartAbstract;
import models.file.CloudFile;
import models.file.FilePart;
import services.CloudFileService;
import services.FilePartService;
import utils.Constants;

/**
 * Created by raphabot on 02/02/15.
 */
public class Core {

    private static SimpleEntityManager simpleEntityManager = new SimpleEntityManager(Constants.PERSISTENCE_UNIT_NAME);

    public static boolean encodeSplitUpload(String filePath, List<ProviderAbstract> providers) throws NoSuchAlgorithmException, IOException, Exception {

        //Open file
        File file = new File(filePath);

        //Encode file
        //Calculate MD5
        String md5 = utils.MD5Generator.generate(filePath);

        //Create CloudFile
        String fileName = file.getName();
        CloudFile cloudFile = new CloudFile(fileName, md5);

        //Slipt file
        int numParts = providers.size();
        Splitter splitter = new Splitter(file);
        splitter.split(numParts);

        //Iterate over each splitted file
        for (int i = 0; i < numParts; i++) {
            ProviderAbstract provider = providers.get(i);
            //provider.setup();

            String partFilePath = filePath + ".part." + i;

            //Upload to provider
            String remotePath = provider.uploadFile(partFilePath, fileName + "part" + i);

            //Calculate MD5
            md5 = utils.MD5Generator.generate(partFilePath);

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

    public static boolean downloadMergeDecode(CloudFileAbstract cloudFile) {

        List<FilePartAbstract> fileParts = cloudFile.getFileParts();
        int numParts = fileParts.size();
        String fileName = cloudFile.getName();

        //Download files
        int i = 0;
        for (FilePartAbstract filePart : fileParts) {
            ProviderAbstract provider = filePart.getProvider();
            String filePartName = fileName + ".part." + i;
            provider.downloadFile(filePartName, filePart.getRemotePath());
            i++;
        }

        //Merge files
        File file = new File(fileName + ".part.0");
        Splitter splitter = new Splitter(file);
        splitter.unsplit();

        //Decode file
        return true;
    }
}
