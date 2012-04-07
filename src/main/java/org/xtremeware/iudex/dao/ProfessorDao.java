/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.ProfessorEntity;

/**
 * DAO for the Professor entities. Implements additionally some useful finders
 * by name and subject
 * @author juan
 */
public class ProfessorDao extends Dao<ProfessorEntity> {

    /**
     * Professors finder according to a required name
     * @param em the entity manager
     * @param name Professor's Firstname or lastname
     * @return List of professors whose firstname or lastname are equal to the specified
     */
    public List<ProfessorEntity> getByNameLike(EntityManager em, String name) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        try {
            return em.createNamedQuery("getProfessorByNameLike").setParameter("name", name).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Professors finder according to the subjects they offer
     * @param em the entity manager
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    public List<ProfessorEntity> getBySubjectId(EntityManager em, long subjectId) {

        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }

        try {
            return em.createNamedQuery("getProfessorBySubjectId").setParameter("subjectId", subjectId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
