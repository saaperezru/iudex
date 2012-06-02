package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface CourseRatingDao extends CrudDao<CourseRatingEntity> {

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * course.
     *
     * @param entityManager the entity manager
     * @param courseId id of the course
     * @return a list of CourseRating entities with a course identified by
     * courseId
     */
    List<CourseRatingEntity> getByCourseId(EntityManager entityManager, Long courseId)
            throws DataBaseException;

    /**
     * Returns a CourseRating entity which have the given course and user mapped
     * by the respective Id.
     *
     * @param entityManager the entity manager
     * @param courseId id of the course
     * @param userId id of the user
     * @return CourseRatingEntity with the indicated user and course
     */
    CourseRatingEntity getByCourseIdAndUserId(EntityManager entityManager,
            Long courseId, Long userId)
            throws DataBaseException;

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * user.
     *
     * @param entityManager the entity manager
     * @param userId id of the user
     * @return a list of CourseRating entities with a course identified by
     * userId
     */
    List<CourseRatingEntity> getByUserId(EntityManager entityManager, Long userId)
            throws DataBaseException;
}
