package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CourseRatingDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 *
 * @author josebermeo
 */
public class CourseRatingsService extends SimpleCrudService<CourseRatingVo> {

    /**
     * CourseRatingsService constructor
     *
     * @param daoFactory
     */
    public CourseRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaCourseRatingDao to be used.
     *
     * @return JpaCourseRatingDao
     */
    @Override
    protected CrudDao<CourseRatingVo, CourseRatingEntity> getDao() {
        return getDaoFactory().getCourseRatingDao();
    }

    /**
     * Validate the provided CourseRatingVo, if the CourseRatingVo is not
     * correct the methods throws an exception
     *
     * @param em EntityManager
     * @param vo CourseRatingVo to validate
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(DataAccessAdapter em, CourseRatingVo vo) throws InvalidVoException, DataAccessException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null CourseRatingVo");
        }
        if (vo.getCourseId() == null) {
            throw new InvalidVoException("Null courseId in the provided CourseRatingVo");
        }
        if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId()) == null) {
            throw new InvalidVoException("No such course  associated with CourseRatingVo.courseId");
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

    /**
     * Returns a CourseRatingVo associated with the provided userId and courseId
     *
     * @param em EntityManager
     * @param courseId course identifier
     * @param userId user identifier
     * @return CourseRatingVo
     */
    public CourseRatingVo getByCourseIdAndUserId(DataAccessAdapter em, long courseId, long userId) throws DataAccessException {
        return ((CourseRatingDao) this.getDao()).getByCourseIdAndUserId(em, courseId, userId);
    }
}
