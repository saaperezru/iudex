package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.CourseRatingDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 * JpaDao for CourseRating value objects. Implements additionally some useful
 * finders by associated course id, user id.
 *
 * @author josebermeo
 */
public class JpaCourseRatingDao extends JpaCrudDao<CourseRatingVo, CourseRatingEntity> implements CourseRatingDao<EntityManager> {

    /**
     * Returns a CourseRating entity using the information in the provided
     * CourseRating value object.
     * 
     * @param em the data access adapter
     * @param vo the CourseRating value object
     * @return the CourseRating entity
     */
    @Override
    protected CourseRatingEntity voToEntity(DataAccessAdapter<EntityManager> em, CourseRatingVo vo) {
        CourseRatingEntity courseRatingEntity = new CourseRatingEntity();
        
        courseRatingEntity.setId(vo.getId());
        courseRatingEntity.setValue(vo.getValue());

        courseRatingEntity.setCourse(em.getDataAccess().getReference(CourseEntity.class, vo.getCourseId()));
        courseRatingEntity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUserId()));

        return courseRatingEntity;
    }

    @Override
    protected Class getEntityClass() {
        return CourseRatingEntity.class;
    }

    /**
     * Returns a list of CourseRating value objects which has the same indicated
     * course.
     *
     * @param em the data access adapter
     * @param courseId id of the course
     * @return a list of CourseRatingVo with a course identified by courseId
     */
    @Override
    public List<CourseRatingVo> getByCourseId(DataAccessAdapter<EntityManager> em, Long courseId) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseRatingByCourseId").setParameter("courseId", courseId).getResultList());
    }

    /**
     * Returns a CourseRating value object which have the given course and user
     * mapped by the respective Ids.
     *
     * @param em the data access adapter
     * @param courseId id of the user
     * @param userId
     * @return CourseRatingEntity with the indicated user and course
     */
    @Override
    public CourseRatingVo getByCourseIdAndUserId(DataAccessAdapter<EntityManager> em, Long courseId, Long userId) {
        checkDataAccessAdapter(em);
        try {
            return ((CourseRatingEntity) em.getDataAccess().createNamedQuery("getCourseRatingByCourseIdAndUserId").setParameter("courseId", courseId).setParameter("userId", userId).getSingleResult()).toVo();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
    
    /**
     * Returns a list of CourseRating value objects which has the same indicated
     * user.
     * @param em the data access adapter
     * @param userId id of the user
     * @return a list of CourseRating value abjects with a course identified by
     * userId
     */
    @Override
    public List<CourseRatingVo> getByUserId(DataAccessAdapter<EntityManager> em, Long userId) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseRatingByUserId").setParameter("userId", userId).getResultList());
    }
}
