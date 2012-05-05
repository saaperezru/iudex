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
public class SubjectRatingDao extends CrudDao<SubjectRatingEntity> implements SubjectRatingDaoInterface {

    /**
     * Returns a list of SubjectRatings entities given the subject
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingEntity
     */
    @Override
    public List<SubjectRatingEntity> getBySubjectId(EntityManager em, Long subjectId) {
        checkEntityManager(em);
        return em.createNamedQuery("getSubjectRatingBySubjectId", SubjectRatingEntity.class).
                setParameter("subjectId", subjectId).getResultList();
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
    @Override
    public SubjectRatingEntity getBySubjectIdAndUserId(EntityManager em, Long subjectId, Long userId) {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getSubjectRatingBySubjectIdAndUserId", SubjectRatingEntity.class).
                    setParameter("subjectId", subjectId).setParameter("userId", userId).getSingleResult();
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
    @Override
    public List<SubjectRatingEntity> getByUserId(EntityManager em, Long userId) {
        checkEntityManager(em);
        return em.createNamedQuery("getUserRatingBySubjectId", SubjectRatingEntity.class).
                setParameter("userId", userId).getResultList();
    }

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    @Override
    public RatingSummaryVo getSummary(EntityManager em, Long subjectId) {
        checkEntityManager(em);
        RatingSummaryVo rsv = new RatingSummaryVo();

        try {
            rsv.setPositive(em.createNamedQuery("countPositiveSubjectRating", Long.class).
                    setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        try {
            rsv.setNegative(em.createNamedQuery("countNegativeSubjectRating", Long.class).
                    setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
