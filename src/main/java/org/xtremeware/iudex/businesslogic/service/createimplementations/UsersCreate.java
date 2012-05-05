/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service.createimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.CreateInterface;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.UserEntity;
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
    public UserEntity create(EntityManager em, UserEntity entity) {
        //It is not possible to create users that are already active
        entity.setActive(false);
        //Hash password
        entity.setPassword(SecurityHelper.hashPassword(entity.getPassword()));
        //Create confirmation key
        ConfirmationKeyEntity confirmationKeyEntity = new ConfirmationKeyEntity();
        //Set expiration date for one day after creation
        Calendar expiration = new GregorianCalendar();
        expiration.add(Calendar.DAY_OF_MONTH, 1);
        confirmationKeyEntity.setExpirationDate(expiration.getTime());
        //Associate confirmation key with user
        entity.setConfirmationKey(confirmationKeyEntity);
        confirmationKeyEntity.setUser(entity);

        entity = getDaoFactory().getUserDao().persist(em, entity);
        confirmationKeyEntity.setId(entity.getId());
        //persist confirmation key
        getDaoFactory().getConfirmationKeyDao().persist(em, confirmationKeyEntity);

        return entity;
        
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
    
}
