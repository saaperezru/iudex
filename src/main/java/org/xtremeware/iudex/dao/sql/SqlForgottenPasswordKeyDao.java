package org.xtremeware.iudex.dao.sql;

import javax.persistence.*;
import org.xtremeware.iudex.dao.ForgottenPasswordKeyDao;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.entity.ForgottenPasswordKeyEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author healarconr
 */
public class SqlForgottenPasswordKeyDao extends SqlCrudDao<ForgottenPasswordKeyEntity>
        implements ForgottenPasswordKeyDao {
	
	public SqlForgottenPasswordKeyDao(Delete delete) {
        super(delete);
    }

    @Override
    protected Class getEntityClass() {
        return ForgottenPasswordKeyEntity.class;
    }

    /**
     * Returns a ForgottenPasswordKeyEntity which key matches the given key.
     *
     * @param em the entity manager
     * @param key the key reference to search
     * @return the ForgottenPasswordKeyEntity with the given key
     */
    @Override
    public ForgottenPasswordKeyEntity getByKey(EntityManager em, String key)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getForgottenPasswordKeyByKey",
                    ForgottenPasswordKeyEntity.class).
                    setParameter("key", key).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Returns a ForgottenPasswordKeyEntity associated with the give user
     *
     * @param em the entity manager
     * @param userName the name of the user
     * @return the ForgottenPasswordKeyEntity
     */
    @Override
    public ForgottenPasswordKeyEntity getByUserName(EntityManager em,
            String userName) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getForgottenPasswordKeyByUserName",
                    ForgottenPasswordKeyEntity.class).
                    setParameter("userName", userName).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }
}
