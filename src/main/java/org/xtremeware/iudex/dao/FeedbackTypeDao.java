package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for feedback type entities.
 *
 * @author healarconr
 */
public interface FeedbackTypeDao extends CrudDao<FeedbackTypeEntity> {

    /**
     * Returns a feedback type entity which name matches the given name
     *
     * @param entityManager the entity manager
     * @param name the name
     * @return a feedback type entity
     */
    public FeedbackTypeEntity getByName(EntityManager entityManager, String feedbackTypeName)
            throws DataBaseException;

    public List<FeedbackTypeEntity> getAll(EntityManager entityManager)
            throws DataBaseException;
}
