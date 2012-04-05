package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class SubjectRatingsDao extends Dao<SubjectRatingEntity> {

    /**
     * Returns a list of SubjectRatings entities given the subject
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingEntity
     */
    public List<SubjectRatingEntity> getBySubjectId(EntityManager em, Long subjectId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createQuery("getBySubjectId").setParameter("subjectId", subjectId).getResultList();
    }

    /**
     * Returns a rating given by a user, identified by userId, to a subject,
     * identified by subjectId.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity
     */
    public SubjectRatingEntity getBySubjectIdAndUserId(EntityManager em, Long subjectId, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try {
            return (SubjectRatingEntity) em.createQuery("getBySubjectIdAndUserId").setParameter("subjectId", subjectId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    public RatingSummaryVo getSummary(EntityManager em, Long subjectId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        RatingSummaryVo rsv = new RatingSummaryVo();

        Query q = em.createQuery("SELECT COUNT result FROM SubjectRating result "
                + "WHERE result.subject.id = :subjectId AND result.value = 1").setParameter("subjectId", subjectId);
        try {
            rsv.setPositive(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        q = em.createQuery("SELECT COUNT result FROM SubjectRating result "
                + "WHERE result.subject.id = :subjectId AND result.value = -1").setParameter("subjectId", subjectId);
        try {
            rsv.setNegative(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
