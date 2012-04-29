package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.UserDao;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.UserVo;

/**
 * JPADAO for the UserVo. Implements additionally some useful finders by
 * username and password
 *
 * @author juan
 */
public class JpaUserDao extends JpaCrudDao<UserVo, UserEntity> implements UserDao<EntityManager> {

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
    public UserVo getByUsernameAndPassword(DataAccessAdapter<EntityManager> em, String username, String password) throws DataAccessException {
        checkDataAccessAdapter(em);
        try {
            return em.getDataAccess().createNamedQuery("getUserByUsernameAndPassword", getEntityClass()).setParameter("userName", username).setParameter("password", password).getSingleResult().toVo();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    protected UserEntity voToEntity(DataAccessAdapter<EntityManager> em, UserVo vo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        userEntity.setFirstName(vo.getFirstName());
        userEntity.setLastName(vo.getLastName());
        userEntity.setUserName(vo.getUserName());
        userEntity.setPassword(vo.getPassword());
        userEntity.setRol(vo.getRole());
        userEntity.setActive(vo.isActive());

        ArrayList<ProgramEntity> arrayList = new ArrayList<ProgramEntity>();
        for (Long programId : vo.getProgramsId()) {
            arrayList.add(em.getDataAccess().getReference(ProgramEntity.class, programId));
        }

        userEntity.setPrograms(arrayList);
        return userEntity;
    }
}
