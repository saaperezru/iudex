package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;

/**
 * DAO for feedback type entities. Implements additionally an useful finder by
 * name.
 *
 * @author healarconr
 */
public class FeedbackTypeDao extends Dao<FeedbackTypeEntity> {

    /**
     * Returns a feedback type entity which name matches the given name
     *
     * @param em the entity manager
     * @param name the name
     * @return a feedback type entity
     */
    public FeedbackTypeEntity getByName(EntityManager em, String name) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try {
            return (FeedbackTypeEntity) em.createNamedQuery("getByName", FeedbackTypeEntity.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
