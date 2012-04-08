package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class SubjectRatingDao extends Dao<SubjectRatingEntity> {

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
        return em.createQuery("getSubjectRatingBySubjectId").setParameter("subjectId", subjectId).getResultList();
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
            return (SubjectRatingEntity) em.createQuery("getSubjectRatingBySubjectIdAndUserId").setParameter("subjectId", subjectId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * Returns a list of SubjectRatings entities given the user
     *
     * @param em the entity manager
     * @param userId user id
     * @return list of SubjectRatingEntity
     */
    public List<SubjectRatingEntity> getByUserId(EntityManager em, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createQuery("getUserRatingBySubjectId").setParameter("userId", userId).getResultList();
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

        try {
            rsv.setPositive(((Integer) em.createQuery("countPositiveSubjectRating").setParameter("subjectId", subjectId).getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        try {
            rsv.setNegative(((Integer) em.createQuery("countNegativeSubjectRating").setParameter("subjectId", subjectId).getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
