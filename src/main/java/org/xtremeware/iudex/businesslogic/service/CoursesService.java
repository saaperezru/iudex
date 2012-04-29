package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CourseDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.CourseVo;

public class CoursesService extends SimpleCrudService<CourseVo, CourseEntity> {

    public CoursesService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public void validateVo(DataAccessAdapter em, CourseVo vo) throws InvalidVoException, DataAccessException {
        if (em == null) {
            throw new IllegalArgumentException("DataAccessAdapter em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null CourseVo");
        }
        if (vo.getPeriodId() == null) {
            throw new InvalidVoException("Long periodId in the provided CourseVo cannot be null");
        }
        if (vo.getProfessorId() == null) {
            throw new InvalidVoException("Long professorId in the provided CourseVo cannot be null");
        }
        if (vo.getSubjectId() == null) {
            throw new InvalidVoException("Long subjectId in the provided CourseVo cannot be null");
        }
        if (vo.getRatingCount() == null) {
            throw new InvalidVoException("Long ratingCount in the provided CourseVo cannot be null");
        }
        if (vo.getRatingAverage() == null) {
            throw new InvalidVoException("Double ratingAverage in the provided CourseVo cannot be null");
        }
        if (getDaoFactory().getProfessorDao().getById(em, vo.getProfessorId()) == null) {
            throw new InvalidVoException("Long professorId in the provided CourseVo must correspond to an existing professor entity in the database");
        }
        if (getDaoFactory().getSubjectDao().getById(em, vo.getSubjectId()) == null) {
            throw new InvalidVoException("Long subjectId in the provided CourseVo must correspond to an existing subject entity in the database");
        }
        if (getDaoFactory().getPeriodDao().getById(em, vo.getPeriodId()) == null) {
            throw new InvalidVoException("Long periodId in the provided CourseVo must correspond to an existing period entity in the database");
        }
        if (vo.getRatingCount() < 0) {
            throw new InvalidVoException("Long ratingCount in the provided CourseVo must be greater than one");
        }
        if (vo.getRatingAverage() < 0) {
            throw new InvalidVoException("Double ratingAverage in the provided CourseVo must be greater than one");
        }
    }

    public List<CourseVo> getByProfessorId(DataAccessAdapter em, long professorId) throws DataAccessException {
        return ((CourseDao) getDao()).getByPeriodId(em, professorId);
    }

    public List<CourseVo> getBySubjectId(DataAccessAdapter em, long subjectId) throws DataAccessException {
        return ((CourseDao) getDao()).getBySubjectId(em, subjectId);
    }

    public List<CourseVo> getSimilarCourses(DataAccessAdapter em, String professorName, String subjectName, long preiodId) throws DataAccessException {
        if(professorName == null || subjectName==null){
            throw new NullPointerException("Professor Name and  subject Name cannot be null");
        }
        return ((CourseDao) getDao()).getCoursesByProfessorNameLikeAndSubjectNameLike(em, professorName, subjectName, preiodId);
    }

    @Override
    public void remove(DataAccessAdapter em, long courseId) throws DataAccessException {

        List<CourseRatingEntity> courseRatings = getDaoFactory().getCourseRatingDao().getByCourseId(em, courseId);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoFactory().getCourseRatingDao().remove(em, rating.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CommentEntity> comments = getDaoFactory().getCommentDao().getByCourseId(em, courseId);

        CommentsService commentService = Config.getInstance().getServiceFactory().createCommentsService();
        for (CommentEntity comment : comments) {
            commentService.remove(em, comment.getId());
        }

        getDaoFactory().getCourseDao().remove(em, courseId);

    }

    @Override
    protected CrudDao<CourseVo, CourseEntity> getDao() {
        return getDaoFactory().getCourseDao();
    }
}
