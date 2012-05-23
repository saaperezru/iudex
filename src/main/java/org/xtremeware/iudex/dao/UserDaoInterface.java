package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the User Entities.
 *
 * @author juan
 */
public interface UserDaoInterface extends CrudDaoInterface<UserEntity> {

    /**
     * Returns a UserEntity that corresponds to the user whose username and
     * password are like the specified ones
     *
     * @param em the entity manager
     * @param userName String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    public UserEntity getByUserNameAndPassword(EntityManager em, String userName,
            String password) throws DataBaseException;

    /**
     * Returns a UserEntity associated with the given user name
     *
     * @param em the entity manager
     * @param userName the user name
     * @return the UserEntity
     * @throws DataBaseException
     */
    public UserEntity getByUserName(EntityManager em, String userName) throws
            DataBaseException;
}
