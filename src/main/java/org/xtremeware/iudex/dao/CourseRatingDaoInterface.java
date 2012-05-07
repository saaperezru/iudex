package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CourseRatingEntity;

/**
 *
 * @author josebermeo
 */
public interface CourseRatingDaoInterface extends CrudDaoInterface<CourseRatingEntity> {

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * course.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @return a list of CourseRating entities with a course identified by
     * courseId
     */
    public List<CourseRatingEntity> getByCourseId(EntityManager em, Long courseId);

    /**
     * Returns a CourseRating entity which have the given course and user mapped
     * by the respective Id.
     *
     * @param em the entity manager
     * @param courseId id of the course
     * @param userId id of the user
     * @return CourseRatingEntity with the indicated user and course
     */
    public CourseRatingEntity getByCourseIdAndUserId(EntityManager em, Long courseId, Long userId);

    /**
     * Returns a list of CourseRating entities which has the same indicated
     * user.
     *
     * @param em the entity manager
     * @param userId id of the user
     * @return a list of CourseRating entities with a course identified by
     * userId
     */
    public List<CourseRatingEntity> getByUserId(EntityManager em, Long userId);
}
