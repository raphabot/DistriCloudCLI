/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO.Utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author raphabot
 */
public class UtilsDAO {
    
    private static Session currentSession;
    private static Transaction currentTransaction;

    public static Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public static Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public static void closeCurrentSession() {
        currentSession.close();
    }

    public static void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        EntityManagerFactory emf =  Persistence.createEntityManagerFactory("hsqldb");
        EntityManager em = emf.createEntityManager();
        
        
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        return sessionFactory;
    }

    public static Session getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(Session currentSession) {
        UtilsDAO.currentSession = currentSession;
    }

    public static Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public static void setCurrentTransaction(Transaction currentTransaction) {
        UtilsDAO.currentTransaction = currentTransaction;
    }

}
