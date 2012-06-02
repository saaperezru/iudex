package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the Feedback entities.
 *
 * @author saaperezru
 */
public interface FeedbackDao extends CrudDao<FeedbackEntity> {

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
    List<FeedbackEntity> getByTypeId(EntityManager entityManager, long feedbackTypeId)
            throws DataBaseException;

    List<FeedbackEntity> getByContentLike(EntityManager entityManager, String query)
            throws DataBaseException;

    List<FeedbackEntity> getAll(EntityManager entityManager)
            throws DataBaseException;
}
