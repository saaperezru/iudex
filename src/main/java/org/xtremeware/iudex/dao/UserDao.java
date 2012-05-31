package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the User Entities.
 *
 * @author juan
 */
public interface UserDao extends CrudDao<UserEntity> {

    /**
     * Returns a value object that corresponds to the user whose username and
     * password are like the specified ones
     *
     * @param entityManager the entity manager
     * @param username String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    UserEntity getByUsernameAndPassword(EntityManager entityManager,
            String username, String password) throws DataBaseException;

    /**
     * Returns a UserEntity associated with the given user name
     *
     * @param em the entity manager
     * @param userName the user name
     * @return the UserEntity
     * @throws DataBaseException
     */
    UserEntity getByUserName(EntityManager em, String userName) throws
            DataBaseException;
}
