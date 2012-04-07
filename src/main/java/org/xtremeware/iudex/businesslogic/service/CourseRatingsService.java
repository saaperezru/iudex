package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CourseRatingDao;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 *
 * @author josebermeo
 */
public class CourseRatingsService extends SimpleCrudService<CourseRatingVo, CourseRatingEntity> {

    public CourseRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    protected Dao<CourseRatingEntity> getDao() {
        return getDaoFactory().getCourseRatingDao();
    }

    @Override
    public void validateVo(EntityManager em, CourseRatingVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null CourseRatingVo");
        }
        if (vo.getCourseId() == null) {
            throw new InvalidVoException("Null uourseId in the provided CourseRatingVo");
        }
        if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId()) == null) {
            throw new InvalidVoException("No such course associeted associated with CourseRatingVo.courseId");
        }
        if (vo.getUserId() == null) {
            throw new InvalidVoException("Null userId in the provided CourseRatingVo");
        }
        if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) == null) {
            throw new InvalidVoException("No such user associated with CourseRatingVo.userId");
        }
        if (vo.getValue() < 0.0 || vo.getValue() > 5.0) {
            throw new InvalidVoException("int Value in the provided CourseRatingVo must be less than or equal to 5.0 and greater than or equal to 0.0");
        }
    }

    @Override
    public CourseRatingEntity voToEntity(EntityManager em, CourseRatingVo vo) throws InvalidVoException{
        
        validateVo(em, vo);
        
        CourseRatingEntity courseRatingEntity = new CourseRatingEntity();
        courseRatingEntity.setId(vo.getId());
        courseRatingEntity.setValue(vo.getValue());

        courseRatingEntity.setCourse(getDaoFactory().getCourseDao().getById(em, vo.getCourseId()));

        courseRatingEntity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));

        return courseRatingEntity;
    }

    public CourseRatingVo getByCourseIdAndUserId(EntityManager em, Long courseId, Long userId) {
        CourseRatingEntity courseRatingEntity = ((CourseRatingDao) this.getDao()).getByCourseIdAndUserId(em, courseId, userId);
        if (courseRatingEntity == null) {
            return null;
        }
        return courseRatingEntity.toVo();
    }
}
