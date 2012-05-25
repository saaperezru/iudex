package org.xtremeware.iudex.businesslogic.service.createimplementations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.SecurityHelper;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;

/**
 *
 * @author josebermeo
 */
public class UsersCreate implements Create<UserEntity> {
    private AbstractDaoBuilder daoFactory;

    public UsersCreate(AbstractDaoBuilder daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UserEntity create(EntityManager entityManager, UserEntity entity)
            throws DataBaseException, DuplicityException {
        //It is not possible to create users that are already active
        entity.setActive(false);
        //Hash password
        entity.setPassword(SecurityHelper.hashPassword(entity.getPassword()));
        //Create confirmation key
        ConfirmationKeyEntity confirmationKeyEntity = new ConfirmationKeyEntity();
        //Set expiration date for one day after creation
        Calendar expiration = new GregorianCalendar();
        // TODO: The expiration period should be configurable
        expiration.add(Calendar.DAY_OF_MONTH, 1);
        confirmationKeyEntity.setExpirationDate(expiration.getTime());
        confirmationKeyEntity.setConfirmationKey(SecurityHelper.generateConfirmationKey());
        //Associate confirmation key with user
        entity.setConfirmationKey(confirmationKeyEntity);

        entity = getDaoFactory().getUserDao().persist(entityManager, entity);
        confirmationKeyEntity.setUser(entity);
        //persist confirmation key
        getDaoFactory().getConfirmationKeyDao().persist(entityManager, confirmationKeyEntity);
        return entity;
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }
    
}
