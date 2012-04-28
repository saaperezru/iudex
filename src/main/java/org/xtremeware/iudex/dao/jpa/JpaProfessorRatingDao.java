/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao.jpa;

import org.xtremeware.iudex.dao.jpa.JpaCrudDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO for the ProfessorRating entities. Implements additionally some useful finders
 * by professor and user
 * 
 * @author juan
 */
public class JpaProfessorRatingDao extends JpaCrudDao<ProfessorRatingEntity> {

    /**
     * Professors ratings finder according to a specified professor
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    public List<ProfessorRatingEntity> getByProfessorId(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        return em.createNamedQuery("getRatingByProfessorId").setParameter("professor", professorId).getResultList();

    }

    /**
     * Professor ratings finder according to a professor and a student
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has subbmited to a professor
     */
    public ProfessorRatingEntity getByProfessorIdAndUserId(EntityManager em, long professorId, long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        try {
            return (ProfessorRatingEntity) em.createNamedQuery("getRatingByProfessorIdAndUserId").setParameter("professor", professorId).setParameter("user", userId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
    
    /**
     * Professors ratings finder according to a specified user
     *
     * @param em the entity manager
     * @param userId user's ID
     * @return A list with all ratings associated to the specified user
     */
    public List<ProfessorRatingEntity> getByUserId(EntityManager em, long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        return em.createNamedQuery("getRatingByUserId").setParameter("user", userId).getResultList();

    }   

    /**
     * Proffesor rating summary calculator
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained possitive and negative ratings
     */
    public RatingSummaryVo getSummary(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        RatingSummaryVo result = new RatingSummaryVo();

        Query q = em.createQuery("SELECT COUNT(r) FROM ProfessorRating r WHERE r.value = 1");

        try {
            result.setPositive(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException e) {
            return null;
        }

        q = em.createQuery("SELECT COUNT(r) FROM ProfessorRating r WHERE r.value = -1");

        try {
            result.setNegative(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException e) {
            return null;
        }

        return result;
    }
}
