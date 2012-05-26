package org.xtremeware.iudex.businesslogic.service.createimplementations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.CreateInterface;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.SecurityHelper;

/**
 *
 * @author josebermeo
 */
public class UsersCreate implements CreateInterface<UserEntity> {
    private AbstractDaoFactory daoFactory;

    public UsersCreate(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UserEntity create(EntityManager em, UserEntity entity) throws DataBaseException, DuplicityException {
        //It is not possible to create users that are already active
        entity.setActive(false);
        //Hash password
        entity.setPassword(SecurityHelper.hashPassword(entity.getPassword()));
        //Create confirmation key
        ConfirmationKeyEntity confirmationKeyEntity = new ConfirmationKeyEntity();
        //Set expiration date for one day after creation
        Calendar expiration = new GregorianCalendar();
        expiration.add(Calendar.DAY_OF_MONTH, Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_KEYS_EXPIRATION)));
        confirmationKeyEntity.setExpirationDate(expiration.getTime());
        confirmationKeyEntity.setConfirmationKey(SecurityHelper.generateMailingKey());
        //Associate confirmation key with user
        entity.setConfirmationKey(confirmationKeyEntity);

        entity = getDaoFactory().getUserDao().persist(em, entity);
        confirmationKeyEntity.setUser(entity);
        //persist confirmation key
        getDaoFactory().getConfirmationKeyDao().persist(em, confirmationKeyEntity);

        return entity;
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
    
}
