package com.mycompany.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.mycompany.entity.Users;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UsersEJB {

    @Resource
    private UserTransaction userTransaction;

    @PersistenceContext(unitName="test-unit")
    private EntityManager em;
    
    public List<Users> allUsers(){
        TypedQuery<Users> sorgu = em.createNamedQuery("allUsers", Users.class);
        return sorgu.getResultList();
    }
    
    public Users getXUser(String username){
        return em.find(Users.class, username);
    }
    
    public Users getXEmail(String email){
        return em.find(Users.class, email);
    }
    
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users saveUser(Users user){
        try {
            userTransaction.begin();
            em.persist(user);
            em.flush();
            userTransaction.commit();
        } catch (NotSupportedException | HeuristicMixedException | HeuristicRollbackException | RollbackException | SystemException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users mergeUser(Users user){
        
        System.out.println("--------------------5--------------------------------");
        System.out.println(user.getId());
        System.out.println("----------------------6------------------------------");
        em.merge(em.find(Users.class,user.getId()));
        
        System.out.println("------------------------7----------------------------");
        return user;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteUsers(Users user) throws Exception{
        try
        {
            em.remove(em.merge(user));
            em.flush();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users editUsers(Users user) throws Exception {
        try
        {
            em.merge(user);
            em.flush();
            return user;
        }
        catch(Exception e)
        {
            
            System.out.println(e);
            return null;
        }
    }
    
}
