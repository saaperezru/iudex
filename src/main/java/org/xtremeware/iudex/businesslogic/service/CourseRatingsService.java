package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
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
    public void validateVo(CourseRatingVo vo) throws InvalidVoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CourseRatingEntity voToEntity(CourseRatingVo vo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CourseRatingVo getByCourseIdAndUserId(EntityManager em, Long courseId, Long userId) {
        return null;
    }
}
