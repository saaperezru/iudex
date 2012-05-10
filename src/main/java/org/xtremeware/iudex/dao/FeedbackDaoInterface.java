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
public interface FeedbackDaoInterface extends CrudDaoInterface<FeedbackEntity> {

    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * feedbackTypeId
     *
     * @param em EntityManager with which the entities will be searched
     * @param feedbackTypeId Feedback type identifier to look for in feedback
     * entities.
     * @return The list of found feedbacks.
     */
    public List<FeedbackEntity> getByTypeId(EntityManager em, long feedbackTypeId)
            throws DataBaseException;

    public List<FeedbackEntity> getByContentLike(EntityManager em, String query)
            throws DataBaseException;

    public List<FeedbackEntity> getAll(EntityManager em)throws DataBaseException;
}
