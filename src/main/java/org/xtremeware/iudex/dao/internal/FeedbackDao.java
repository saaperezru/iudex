package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.FeedbackDaoInterface;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the Feedback entities. Implements additionally a useful finder by
 * Feedback type.
 *
 * @author saaperezru
 */
public class FeedbackDao extends CrudDao<FeedbackEntity> implements FeedbackDaoInterface {

    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * feedbackTypeId
     *
     * @param em EntityManager with which the entities will be searched
     * @param feedbackTypeId Feedback type identifier to look for in feedback
     * entities.
     * @return The list of found feedbacks.
     */
    @Override
    public List<FeedbackEntity> getByTypeId(EntityManager em, long feedbackTypeId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getByTypeId", FeedbackEntity.class).setParameter("feedbackTypeId", feedbackTypeId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<FeedbackEntity> getByContentLike(EntityManager em, String query)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getFeedbackByContentLike", FeedbackEntity.class).setParameter("query", "%" + query + "%").getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return FeedbackEntity.class;
    }
}
