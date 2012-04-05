package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class SubjectRatingsDao extends Dao<SubjectRatingEntity> {
    /**
     * 
     * @param em
     * @param subjectId
     * @return 
     */
    public List<SubjectRatingEntity> getBySubjectId(EntityManager em, Long subjectId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createQuery("getBySubjectId").setParameter("SI", subjectId).getResultList();
    }
    /**
     * 
     * @param em
     * @param subjectId
     * @param userId
     * @return 
     */
    public SubjectRatingEntity getBySubjectIdAndUserId(EntityManager em, Long subjectId, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return (SubjectRatingEntity) em.createQuery("getBySubjectIdAndUserId").setParameter("SI", subjectId).setParameter("UI", userId).getSingleResult();
    }
    /**
     * 
     * @param em
     * @param subjectId
     * @return 
     */
    public RatingSummaryVo getSummary(EntityManager em, Long subjectId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        RatingSummaryVo rsv = new RatingSummaryVo();

        Query q = em.createQuery("SELECT COUNT result FROM SubjectRatingEntity result "
                + "WHERE result.value = 1");
        rsv.setPositive(((Integer) q.getSingleResult()).intValue());

        q = em.createQuery("SELECT COUNT result FROM SubjectRatingEntity result "
                + "WHERE result.value = -1");
        rsv.setNegative(((Integer) q.getSingleResult()).intValue());

        return rsv;
    }
}
