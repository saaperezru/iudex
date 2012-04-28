package org.xtremeware.iudex.dao.jpa;

import org.xtremeware.iudex.dao.jpa.JpaCrudDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;

/**
 * DAO for feedback type entities. Implements additionally an useful finder by
 * name.
 *
 * @author healarconr
 */
public class JpaFeedbackTypeDao extends JpaCrudDao<FeedbackTypeEntity> {

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
            return em.createNamedQuery("getFeedbackTypeByName", FeedbackTypeEntity.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<FeedbackTypeEntity> getAll(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getAllFeedbackType").getResultList();
    }
}
