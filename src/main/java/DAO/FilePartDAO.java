/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.GenericDAO;
import javax.persistence.EntityManager;
import models.abstracts.FilePartAbstract;

/**
 *
 * @author raphabot
 */
public class FilePartDAO extends GenericDAO<Long, FilePartAbstract>{

    public FilePartDAO(EntityManager entityManager){
        super(entityManager);
    }
   
}
