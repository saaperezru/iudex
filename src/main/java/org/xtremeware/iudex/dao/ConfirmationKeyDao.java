package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the ConfirmationKey entities.
 *
 * @author josebermeo
 */
public interface ConfirmationKeyDao extends CrudDao<ConfirmationKeyEntity> {

    /**
     * Returns a ConfirmationKey entity which key matched with the given string.
     *
     * @param entityManager the entity manager
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKey entity wit the given key
     */
    public ConfirmationKeyEntity getByConfirmationKey(EntityManager entityManager,
            String confirmationKey) throws DataBaseException;
}
