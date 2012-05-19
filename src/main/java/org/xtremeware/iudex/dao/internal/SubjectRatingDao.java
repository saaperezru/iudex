package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.SubjectRatingDaoInterface;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class SubjectRatingDao extends CrudDao<SubjectRatingEntity>
        implements SubjectRatingDaoInterface {

    /**
     * Returns a list of SubjectRatings entities given the subject
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingEntity
     */
    @Override
    public List<SubjectRatingEntity> getBySubjectId(EntityManager em,
            Long subjectId) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getSubjectRatingBySubjectId", SubjectRatingEntity.class).
                    setParameter("subjectId", subjectId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

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
    public SubjectRatingEntity getBySubjectIdAndUserId(EntityManager em,
            Long subjectId, Long userId) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getSubjectRatingBySubjectIdAndUserId", SubjectRatingEntity.class).
                    setParameter("subjectId", subjectId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
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
    public List<SubjectRatingEntity> getByUserId(EntityManager em, Long userId) throws
            DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getUserRatingBySubjectId", SubjectRatingEntity.class).
                    setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    @Override
    public RatingSummaryVo getSummary(EntityManager em, Long subjectId) throws
            DataBaseException {
        checkEntityManager(em);
        RatingSummaryVo rsv = new RatingSummaryVo();

        try {
            rsv.setPositive(em.createNamedQuery("countPositiveSubjectRating", Long.class).
                    setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

        try {
            rsv.setNegative(em.createNamedQuery("countNegativeSubjectRating", Long.class).
                    setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

        return rsv;
    }

    @Override
    protected Class getEntityClass() {
        return SubjectRatingEntity.class;
    }
}
