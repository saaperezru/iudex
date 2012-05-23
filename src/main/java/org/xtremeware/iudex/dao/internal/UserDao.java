package org.xtremeware.iudex.dao.internal;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.UserDaoInterface;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;

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
     * @param userName String containing the username
     * @param password String containing the password
     * @return Value object with required user information
     */
    @Override
    public UserEntity getByUserNameAndPassword(EntityManager em, String userName,
            String password) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getUserByUserNameAndPassword",
                    UserEntity.class).
                    setParameter("userName", userName).setParameter("password",
                    password).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public UserEntity getByUserName(EntityManager em, String userName) throws
            DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getUserByUserName",
                    UserEntity.class).
                    setParameter("userName", userName).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return UserEntity.class;
    }
}
