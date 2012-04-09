/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.UserEntity;

/**
 * DAO for the User Entities. Implements additionally some useful finders by
 * username and password
 * 
 * @author juan
 */
public class UserDao extends Dao<UserEntity> {
    /**
     * Returns a value object that corresponds to the user whose username and
     * password are like the specified ones
     * 
     * @param em the entity manager
     * @param username String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    public UserEntity getByUsernameAndPassword(EntityManager em, String username, String password){
        
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        
        try{
            return (UserEntity) em.createNamedQuery("getUserByUsernameAndPassword").setParameter("userName", username).setParameter("password", password).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
