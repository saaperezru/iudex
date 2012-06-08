package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.dao.FeedbackDao;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the Feedback entities. Implements additionally a useful finder by
 * Feedback type.
 *
 * @author saaperezru
 */
public class SqlFeedbackDao extends SqlCrudDao<FeedbackEntity> implements
        FeedbackDao {

    public SqlFeedbackDao(Delete delete) {
        super(delete);
    }

    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * feedbackTypeId
     *
     * @param entityManager EntityManager with which the entities will be
     * searched
     * @param feedbackTypeId Feedback type identifier to look for in feedback
     * entities.
     * @return The list of found feedbacks.
     */
    @Override
    public List<FeedbackEntity> getByTypeId(EntityManager entityManager,
            long feedbackTypeId, int firstResult, int maxResults)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getByTypeId",
                    FeedbackEntity.class).setParameter("feedbackTypeId",
                    feedbackTypeId).setFirstResult(firstResult).setMaxResults(
                    maxResults).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<FeedbackEntity> getByContentLike(EntityManager entityManager,
            String query)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getFeedbackByContentLike",
                    FeedbackEntity.class).setParameter("query", "%" + query +
                    "%").getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<FeedbackEntity> getAll(EntityManager entityManager,
            int firstResult, int maxResults)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getAllFeedbacks",
                    FeedbackEntity.class).setFirstResult(firstResult).
                    setMaxResults(maxResults).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public long countAll(EntityManager entityManager) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return (Long) entityManager.createNamedQuery("countAllFeedbacks").
                    getSingleResult();
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public long countByTypeId(EntityManager entityManager, long feedbackTypeId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return (Long) entityManager.createNamedQuery(
                    "countFeedbacksByTypeId").setParameter("feedbackTypeId",
                    feedbackTypeId).getSingleResult();
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return FeedbackEntity.class;
    }
}
