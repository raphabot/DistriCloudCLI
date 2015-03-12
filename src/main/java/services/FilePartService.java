/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import DAO.FilePartDAO;
import DAO.Utils.SimpleEntityManager;
import java.util.List;
import models.abstracts.FilePartAbstract;

/**
 *
 * @author raphabot
 */
public class FilePartService {
    
    private FilePartDAO dao;
     
    private SimpleEntityManager simpleEntityManager;
     
    public FilePartService(SimpleEntityManager simpleEntityManager){
        this.simpleEntityManager = simpleEntityManager;
        dao = new FilePartDAO(simpleEntityManager.getEntityManager());
    }
     
    public void save(FilePartAbstract filePart){
        try{
            simpleEntityManager.beginTransaction();
            //provider.validate();
            dao.save(filePart);
            simpleEntityManager.commit();
        }catch(Exception e){
            e.printStackTrace();
            simpleEntityManager.rollBack();
        }
    }
     
    public List<FilePartAbstract> findAll(){
        return dao.findAll();
    }
    
}
