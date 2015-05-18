/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.GenericDAO;
import javax.persistence.EntityManager;
import models.logic.User;

/**
 *
 * @author raphabot
 */
public class UserDAO extends GenericDAO<Long, User>{

    public UserDAO(EntityManager entityManager){
        super(entityManager);
    }
   
}
