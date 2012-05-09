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
     * Returns a value object that corresponds to the user whose username and
     * password are like the specified ones
     *
     * @param em the entity manager
     * @param username String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    public UserEntity getByUsernameAndPassword(EntityManager em, String username,
            String password) throws DataBaseException;
}
