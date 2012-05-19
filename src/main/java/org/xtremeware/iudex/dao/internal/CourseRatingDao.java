package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.CourseRatingDaoInterface;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseRatingDao extends CrudDao<CourseRatingEntity> implements CourseRatingDaoInterface {

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * course.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @return a list of CourseRating entities with a course identified by
     * courseId
     */
    @Override
    public List<CourseRatingEntity> getByCourseId(EntityManager em, Long courseId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseRatingByCourseId", CourseRatingEntity.class).setParameter("courseId", courseId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a CourseRating entity which have the given course and user mapped
     * by the respective Id.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @param userId id of the user
     * @return CourseRatingEntity with the indicated user and course
     */
    @Override
    public CourseRatingEntity getByCourseIdAndUserId(EntityManager em,
            Long courseId, Long userId) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseRatingByCourseIdAndUserId", CourseRatingEntity.class).
                    setParameter("courseId", courseId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
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
    @Override
    public List<CourseRatingEntity> getByUserId(EntityManager em, Long userId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseRatingByUserId", CourseRatingEntity.class).setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return CourseRatingEntity.class;
    }
}
