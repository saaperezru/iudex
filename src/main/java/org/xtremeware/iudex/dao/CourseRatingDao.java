package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.CourseRatingEntity;

/**
 *
 * @author josebermeo
 */
public class CourseRatingDao extends Dao<CourseRatingEntity> {

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * course.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @return a list of CourseRating entities with a course identified by
     * courseId
     */
    public List<CourseRatingEntity> getByCourseId(EntityManager em, Long courseId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCourseRatingByCourseId").setParameter("courseId", courseId).getResultList();
    }

    /**
     * Returns a CourseRating entity which have the given course and user mapped
     * by the respective Ids.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @param userId id of the user
     * @return CourseRatingEntity with the indicated user and course
     */
    public CourseRatingEntity getByCourseIdAndUserId(EntityManager em, Long courseId, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try {
            return (CourseRatingEntity) em.createNamedQuery("getCourseRatingByCourseIdAndUserId").setParameter("courseId", courseId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
    
    /**
     * Returns a list of CourseRating entities which has the same indicated
     * user.
     *
     * @param em the entity manager
     * @param userId id of the user
     * @return a list of CourseRating entities with a course identified by
     * userId
     */
    public List<CourseRatingEntity> getByUserId(EntityManager em, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCourseRatingByUserId").setParameter("userId", userId).getResultList();
    }
}
