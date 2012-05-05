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
public class UserDao extends CrudDao<UserEntity> implements UserDaoInterface {

    /**
     * Returns a value object that corresponds to the user whose username and
     * password are like the specified ones
     *
     * @param em the entity manager
     * @param username String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    @Override
    public UserEntity getByUsernameAndPassword(EntityManager em, String username, String password) {

        checkEntityManager(em);
        try {
            return em.createNamedQuery("getUserByUsernameAndPassword", UserEntity.class).
                    setParameter("userName", username).setParameter("password", password).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
