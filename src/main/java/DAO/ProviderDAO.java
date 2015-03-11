/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.ProviderDAOInterface;
import DAO.Utils.UtilsDAO;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import models.abstracts.ProviderAbstract;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author raphabot
 */
public class ProviderDAO<T, Id extends Serializable> implements ProviderDAOInterface<ProviderAbstract, Id> {

    @PersistenceContext
    EntityManager entityManager;
    
    public ProviderDAO(){
        
    }
    
    @Override
    public void persist(ProviderAbstract entity) {
        UtilsDAO.getCurrentSession().save(entity);
    }

    @Override
    public void update(ProviderAbstract entity) {
        UtilsDAO.getCurrentSession().update(entity);
    }

    @Override
    public ProviderAbstract findById(Id id){
        //ProviderAbstract provider = (ProviderAbstract) UtilsDAO.getCurrentSession().get(ProviderAbstract.class, id);
        //return provider;
        return entityManager.find( ProviderAbstract.class, id );
    }

    @Override
    public void delete(ProviderAbstract entity) {
        UtilsDAO.getCurrentSession().delete(entity);
    }

    @Override
    public List<ProviderAbstract> findAll() {
        List<ProviderAbstract> providers = (List<ProviderAbstract>) UtilsDAO.getCurrentSession().createQuery("from Providers").list();
        return providers;
    }

    @Override
    public void deleteAll() {
        List<ProviderAbstract> providers = findAll();
        for (ProviderAbstract provider : providers){
            delete(provider);
        }
    }
    
    
    
}
