/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO for the ProfessorRating entities. Implements additionally some useful finders
 * by professor and user
 * 
 * @author juan
 */
public class ProfessorRatingDao extends CrudDao<ProfessorRatingEntity> implements ProfessorRatingDaoInterface{

    /**
     * Professors ratings finder according to a specified professor
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    @Override
    public List<ProfessorRatingEntity> getByProfessorId(EntityManager em, long professorId) {
        checkEntityManager(em);
        return em.createNamedQuery("getRatingByProfessorId", ProfessorRatingEntity.class).setParameter("professor", professorId).getResultList();

    }

    /**
     * Professor ratings finder according to a professor and a student
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has submitted to a professor
     */
    @Override
    public ProfessorRatingEntity getByProfessorIdAndUserId(EntityManager em, long professorId, long userId) {
        checkEntityManager(em);

        try {
            return em.createNamedQuery("getRatingByProfessorIdAndUserId", ProfessorRatingEntity.class).setParameter("professor", professorId).setParameter("user", userId).getSingleResult();
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
    @Override
    public List<ProfessorRatingEntity> getByUserId(EntityManager em, long userId) {
        checkEntityManager(em);
        return em.createNamedQuery("getRatingByUserId", ProfessorRatingEntity.class).setParameter("user", userId).getResultList();

    }   

    /**
     * Professor rating summary calculator
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    @Override
    public RatingSummaryVo getSummary(EntityManager em, long professorId) {
        checkEntityManager(em);

        RatingSummaryVo result = new RatingSummaryVo();

        try {
            result.setPositive(em.createNamedQuery("countPositiveProfessorRating", Long.class).setParameter("professorId", professorId).getSingleResult().intValue());
        } catch (NoResultException e) {
            return null;
        }

        try {
            result.setNegative(em.createNamedQuery("countNegativeProfessorRating", Long.class).setParameter("professorId", professorId).getSingleResult().intValue());
        } catch (NoResultException e) {
            return null;
        }

        return result;
    }
}
