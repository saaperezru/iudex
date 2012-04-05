package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;

/**
 *
 * @author josebermeo
 */
public class ConfimationKeyDao extends Dao<ConfirmationKeyEntity> {

    /**
     * Returns a ConfirmationKey entity which key matched with the given string.
     *
     * @param em the entity manager
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKey entity wit the given key
     */
    public ConfirmationKeyEntity getByConfirmationKey(EntityManager em, String confirmationKey) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try {
            return (ConfirmationKeyEntity) em.createQuery("getByConfirmationKey").setParameter("confirmationKey", confirmationKey).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
}
