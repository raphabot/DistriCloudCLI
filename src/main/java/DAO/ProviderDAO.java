/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.GenericDAO;
import javax.persistence.EntityManager;
import models.abstracts.ProviderAbstract;

/**
 *
 * @author raphabot
 */
public class ProviderDAO extends GenericDAO<Long, ProviderAbstract>{

    public ProviderDAO(EntityManager entityManager){
        super(entityManager);
    }
   
}
