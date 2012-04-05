/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author juan
 */
public class ProfessorRatingDao extends Dao<ProfessorRatingEntity> {

    /**
     * Professors ratings finder according to a specified professor
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    List<ProfessorRatingEntity> getByProfessorId(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        try {
            return em.createNamedQuery("getByProfessorId").setParameter("professor", professorId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Professor ratings finder according to a professor and a student
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has subbmited to a professor
     */
    ProfessorRatingEntity getByProfessorIdAndUserId(EntityManager em, long professorId, long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        try {
            return (ProfessorRatingEntity) em.createNamedQuery("getByProfessorIdAndUserId").setParameter("professor", professorId).setParameter("user", userId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * Proffesor rating summary calculator
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified professor has obtained possitive and negative ratings
     */
    RatingSummaryVo getSummary(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        RatingSummaryVo result = new RatingSummaryVo();

        Query q = em.createQuery("SELECT COUNT r FROM ProfessorRating r WHERE r.value = 1");
        result.setPositive(((Integer) q.getSingleResult()).intValue());

        q = em.createQuery("SELECT COUNT r FROM ProfessorRating r WHERE r.value = -1");
        result.setNegative(((Integer) q.getSingleResult()).intValue());

        return result;
    }
}
