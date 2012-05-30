package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.*;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for feedback type entities. Implements additionally an useful finder by
 * name.
 *
 * @author healarconr
 */
public class SqlFeedbackTypeDao extends SqlCrudDao<FeedbackTypeEntity> implements FeedbackTypeDao {

    public SqlFeedbackTypeDao(Delete delete) {
        super(delete);
    }

    /**
     * Returns a feedback type entity which name matches the given name
     *
     * @param entityManager the entity manager
     * @param name the name
     * @return a feedback type entity
     */
    @Override
    public FeedbackTypeEntity getByName(EntityManager entityManager, String feedbackTypeName)
            throws DataBaseException {
        try {
            return entityManager.createNamedQuery("getFeedbackTypeByName", FeedbackTypeEntity.class).
                    setParameter("name", feedbackTypeName).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<FeedbackTypeEntity> getAll(EntityManager entityManager)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getAllFeedbackType", FeedbackTypeEntity.class).
                    getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return FeedbackTypeEntity.class;
    }
}
