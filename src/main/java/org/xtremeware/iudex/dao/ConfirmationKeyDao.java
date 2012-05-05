package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;

/**
 *
 * @author josebermeo
 */
public class ConfirmationKeyDao extends CrudDao<ConfirmationKeyEntity> implements ConfirmationKeyDaoInterface {

    /**
     * Returns a ConfirmationKey entity which key matched with the given string.
     *
     * @param em the entity manager
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKey entity wit the given key
     */
    @Override
    public ConfirmationKeyEntity getByConfirmationKey(EntityManager em, String confirmationKey) {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getByConfirmationKey", ConfirmationKeyEntity.class).
                    setParameter("confirmationKey", confirmationKey).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
}
