package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.UpdateInterface;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.SecurityHelper;

/**
 *
 * @author healarconr
 */
public class UsersUpdate implements UpdateInterface<UserEntity> {

    private CrudDaoInterface<UserEntity> dao;

    public UsersUpdate(CrudDaoInterface<UserEntity> dao) {
        this.dao = dao;
    }

    @Override
    public UserEntity update(EntityManager em, UserEntity entity) throws DataBaseException {
        UserEntity existingUser = getDao().getById(em, entity.getId());
        if (existingUser != null) {
            entity.setUserName(existingUser.getUserName());
            entity.setRole(existingUser.getRole());
            entity.setPassword(SecurityHelper.hashPassword(entity.getPassword()));
            return getDao().merge(em, entity);
        } else {
            return null;
        }
    }

    public CrudDaoInterface<UserEntity> getDao() {
        return dao;
    }
}
