package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.FeedbackEntity;

/**
 * DAO for the Feedback entities. Implements additionally a useful finder by
 * Feedback type.
 *
 * @author saaperezru
 */
public class FeedbackDao extends Dao<FeedbackEntity> {

    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * feedbackTypeId
     *
     * @param em EntityManager with which the entities will be searched
     * @param feedbackTypeId Feedback type identifier to look for in feedback
     * entities.
     * @return The list of found feedbacks.
     */
    public List<FeedbackEntity> getByTypeId(EntityManager em, long feedbackTypeId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createQuery("getByTypeId").setParameter("feedbackTypeId", feedbackTypeId).getResultList();
    }

    public List<FeedbackEntity> getByContentLike(EntityManager em, String query) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getFeedbackByContentLike").setParameter("query", "%"+query+"%").getResultList();
    }
}
