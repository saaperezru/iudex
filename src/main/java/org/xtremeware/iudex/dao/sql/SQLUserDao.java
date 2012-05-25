package org.xtremeware.iudex.dao.sql;

import javax.persistence.*;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the User Entities. Implements additionally some useful finders by
 * username and password
 *
 * @author juan
 */
public class SQLUserDao extends SQLCrudDao<UserEntity> implements UserDao {

    public SQLUserDao(Remove remove) {
        super(remove);
    }

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
    public UserEntity getByUsernameAndPassword(EntityManager entityManager,
            String username, String password) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getUserByUsernameAndPassword", UserEntity.class).
                    setParameter("userName", username).setParameter("password", password).getSingleResult();
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
