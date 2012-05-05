package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;

/**
 * DAO Interface for the ConfirmationKey entities.
 *
 * @author josebermeo
 */
public interface ConfirmationKeyDaoInterface extends CrudDaoInterface<ConfirmationKeyEntity> {

    /**
     * Returns a ConfirmationKey entity which key matched with the given string.
     *
     * @param em the entity manager
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKey entity wit the given key
     */
    public ConfirmationKeyEntity getByConfirmationKey(EntityManager em, String confirmationKey);
}
