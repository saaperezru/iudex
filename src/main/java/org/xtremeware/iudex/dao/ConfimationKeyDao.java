/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;

/**
 *
 * @author josebermeo
 */
public class ConfimationKeyDao extends Dao<ConfirmationKeyEntity> {

    public ConfirmationKeyEntity getByConfirmationKey(EntityManager em, String confirmationKey) {
        if (em == null)
            throw new IllegalArgumentException("EntityManager em cannot be null");
        return (ConfirmationKeyEntity) em.createQuery("getByConfirmationKey")
                .setParameter("CK", confirmationKey).getSingleResult();
    }
}
