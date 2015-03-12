/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import DAO.ProviderDAO;
import DAO.Utils.SimpleEntityManager;
import java.util.List;
import models.abstracts.ProviderAbstract;

/**
 *
 * @author raphabot
 */
public class ProviderService {
    
    private ProviderDAO dao;
     
    private SimpleEntityManager simpleEntityManager;
     
    public ProviderService(SimpleEntityManager simpleEntityManager){
        this.simpleEntityManager = simpleEntityManager;
        dao = new ProviderDAO(simpleEntityManager.getEntityManager());
    }
     
    public void save(ProviderAbstract provider){
        try{
            simpleEntityManager.beginTransaction();
            //provider.validate();
            dao.save(provider);
            simpleEntityManager.commit();
        }catch(Exception e){
            e.printStackTrace();
            simpleEntityManager.rollBack();
        }
    }
     
    public List<ProviderAbstract> findAll(){
        return dao.findAll();
    }
    
}
