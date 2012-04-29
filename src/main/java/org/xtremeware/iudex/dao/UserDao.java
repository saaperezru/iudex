package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.UserVo;

/**
 * DAO for the UserVo. Implements additionally some useful finders by username
 * and password
 *
 * @author josebermeo
 */
public interface UserDao<E> extends CrudDao<UserVo, E> {

    /**
     * Returns a value object that corresponds to the user whose username and
     * password are like the specified ones
     *
     * @param em the entity manager
     * @param username String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    public UserVo getByUsernameAndPassword(DataAccessAdapter<E> em, String username, String password)throws DataAccessException;
}
