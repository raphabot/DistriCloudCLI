/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import DAO.UserDAO;
import DAO.Utils.SimpleEntityManager;
import java.util.List;
import models.logic.User;

/**
 *
 * @author raphabot
 */
public class UserService {
    
    private UserDAO dao;
     
    private SimpleEntityManager simpleEntityManager;
     
    public UserService(SimpleEntityManager simpleEntityManager){
        this.simpleEntityManager = simpleEntityManager;
        dao = new UserDAO(simpleEntityManager.getEntityManager());
    }
     
    public void save(User user){
        try{
            simpleEntityManager.beginTransaction();
            //provider.validate();
            dao.save(user);
            simpleEntityManager.commit();
        }catch(Exception e){
            e.printStackTrace();
            simpleEntityManager.rollBack();
        }
    }
     
    public List<User> findAll(){
        return dao.findAll();
    }
    
    public User getById(long id){
        User user = dao.getById(id);
        return user;
    }
    
}
