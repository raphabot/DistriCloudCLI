/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import DAO.CloudFileDAO;
import DAO.Utils.SimpleEntityManager;
import java.util.List;
import models.abstracts.CloudFileAbstract;

/**
 *
 * @author raphabot
 */
public class CloudFileService {
    
    private CloudFileDAO dao;
     
    private SimpleEntityManager simpleEntityManager;
     
    public CloudFileService(SimpleEntityManager simpleEntityManager){
        this.simpleEntityManager = simpleEntityManager;
        dao = new CloudFileDAO(simpleEntityManager.getEntityManager());
    }
     
    public void save(CloudFileAbstract cloudFile){
        try{
            simpleEntityManager.beginTransaction();
            //provider.validate();
            dao.save(cloudFile);
            simpleEntityManager.commit();
        }catch(Exception e){
            e.printStackTrace();
            simpleEntityManager.rollBack();
        }
    }
     
    public List<CloudFileAbstract> findAll(){
        return dao.findAll();
    }
    
}
