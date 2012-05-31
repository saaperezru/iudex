package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ForgottenPasswordKeyEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the ForgottenPasswordKey entities.
 *
 * @author healarconr
 */
public interface ForgottenPasswordKeyDao extends
        CrudDao<ForgottenPasswordKeyEntity> {

    /**
     * Returns a ForgottenPasswordKeyEntity which key matches the given key.
     *
     * @param em the entity manager
     * @param key the key reference to search
     * @return the ForgottenPasswordKeyEntity with the given key
     */
    public ForgottenPasswordKeyEntity getByKey(EntityManager em, String key)
            throws DataBaseException;

    /**
     * Returns a ForgottenPasswordKeyEntity associated with the give user
     *
     * @param em the entity manager
     * @param userName the name of the user
     * @return the ForgottenPasswordKeyEntity
     */
    public ForgottenPasswordKeyEntity getByUserName(EntityManager em,
            String userName) throws DataBaseException;
}
