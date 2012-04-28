package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author josebermeo
 */
public interface UserDao<E> extends CrudDao<UserVo,E> {

    public UserVo getByUsernameAndPassword(DataAccessAdapter<E> em, String username, String password);
}
