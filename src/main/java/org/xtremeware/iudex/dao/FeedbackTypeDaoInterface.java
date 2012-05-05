package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;

/**
 * DAO Interface for feedback type entities.
 *
 * @author healarconr
 */
public interface FeedbackTypeDaoInterface extends CrudDaoInterface<FeedbackTypeEntity> {

    /**
     * Returns a feedback type entity which name matches the given name
     *
     * @param em the entity manager
     * @param name the name
     * @return a feedback type entity
     */
    public FeedbackTypeEntity getByName(EntityManager em, String name);

    public List<FeedbackTypeEntity> getAll(EntityManager em);
}
