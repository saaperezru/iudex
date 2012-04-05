/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CourseRatingEntity;

/**
 *
 * @author josebermeo
 */
public class CourseRatingDao extends Dao<CourseRatingEntity> {

    public List<CourseRatingEntity> getByCourseId(EntityManager em, Long courseId) {
        if (em == null)
            throw new IllegalArgumentException("EntityManager em cannot be null");
        return em.createQuery("getByCourseId")
                .setParameter("CI", courseId).getResultList();
    }

    public CourseRatingEntity getByCourseIdAndUserId(EntityManager em, Long courseId, Long userId) {
        if (em == null)
            throw new IllegalArgumentException("EntityManager em cannot be null");
        return (CourseRatingEntity) em.createQuery("getByCourseIdAndUserId")
                .setParameter("CI", courseId).setParameter("UI", userId)
                .getSingleResult();
    }
}
