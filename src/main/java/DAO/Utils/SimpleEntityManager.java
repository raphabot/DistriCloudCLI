package DAO.Utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author gabriel
 * 
* Oct 17, 2013
 * 
* This class encapsulates some work of EntityManager and EntityManagerFactory,
 * doing only the essential
 * 
*/
public class SimpleEntityManager {

    private EntityManager entityManager;
    private EntityManagerFactory factory;

    public SimpleEntityManager(EntityManagerFactory factory) {
        this.factory = factory;
        this.entityManager = factory.createEntityManager();
    }

    public SimpleEntityManager(String persistenceUnitName) {
        factory = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.entityManager = factory.createEntityManager();
    }

    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    public void commit() {
        entityManager.getTransaction().commit();
    }

    /**
     * THIS METHOD NEEDS TO BE ALWAYS CALLED
     */
    public void close() {
        entityManager.close();
        factory.close();
    }

    public void rollBack() {
        entityManager.getTransaction().rollback();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
