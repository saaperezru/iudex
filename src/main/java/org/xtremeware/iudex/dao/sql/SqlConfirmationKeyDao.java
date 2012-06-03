package org.xtremeware.iudex.dao.sql;

import javax.persistence.*;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SqlConfirmationKeyDao extends SqlCrudDao<ConfirmationKeyEntity> implements ConfirmationKeyDao {

    public SqlConfirmationKeyDao( Delete delete) {
        super(delete);
    }

    /**
     * Returns a ConfirmationKey entity which key matched with the given string.
     *
     * @param entityManager the entity manager
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKey entity wit the given key
     */
    @Override
    public ConfirmationKeyEntity getByConfirmationKey(EntityManager entityManager,
            String confirmationKey) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getByConfirmationKey", ConfirmationKeyEntity.class).
                    setParameter("confirmationKey", confirmationKey).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return ConfirmationKeyEntity.class;
    }
}
