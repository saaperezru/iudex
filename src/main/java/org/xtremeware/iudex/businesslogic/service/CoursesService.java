package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.vo.CourseVo;

public class CoursesService extends SimpleCrudService<CourseVo, CourseEntity> {

	public CoursesService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	public void validateVo(EntityManager em, CourseVo course) throws InvalidVoException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
		if (course.getPeriodId() == null) {
			throw new InvalidVoException("Long periodId in the provided CourseVo cannot be null");
		}
		if (course.getProfessorId() == null) {
			throw new InvalidVoException("Long professorId in the provided CourseVo cannot be null");
		}
		if (course.getSubjectId() == null) {
			throw new InvalidVoException("Long subjectId in the provided CourseVo cannot be null");
		}
		if (course.getRatingCount() == null) {
			throw new InvalidVoException("Long ratingCount in the provided CourseVo cannot be null");
		}
		if (course.getRatingAverage() == null) {
			throw new InvalidVoException("Double ratingAverage in the provided CourseVo cannot be null");
		}
		if (getDaoFactory().getProfessorDao().getById(em, course.getProfessorId()) == null) {
			throw new InvalidVoException("Long professorId in the provided CourseVo must correspond to an existing professor entity in the database");
		}
		if (getDaoFactory().getSubjectDao().getById(em, course.getSubjectId()) == null) {
			throw new InvalidVoException("Long subjectId in the provided CourseVo must correspond to an existing subject entity in the database");
		}
		if (getDaoFactory().getPeriodDao().getById(em, course.getPeriodId()) == null) {
			throw new InvalidVoException("Long periodId in the provided CourseVo must correspond to an existing period entity in the database");
		}
		if (course.getRatingCount() < 0) {
			throw new InvalidVoException("Long ratingCount in the provided CourseVo must be greater than one");
		}
		if (course.getRatingAverage() < 0) {
			throw new InvalidVoException("Double ratingAverage in the provided CourseVo must be greater than one");
		}
	}

	public List<CourseVo> getByProfessorId(EntityManager em, long professorId) {
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().getByProfessorId(em, professorId)) {
			list.add(c.toVo());
		}
		return list;
	}

	public List<CourseVo> getBySubjectId(EntityManager em, long subjectId) {
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().getBySubjectId(em, subjectId)) {
			list.add(c.toVo());
		}
		return list;
	}

	public List<CourseVo> getSimilarCourses(EntityManager em, String professorName, String subjectName, Long preiodId) {
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().getCoursesByProfessorNameLikeAndSubjectNameLike(em, professorName, subjectName, preiodId)) {
			list.add(c.toVo());
		}
		return list;
	}

	@Override
	public void remove(EntityManager em, long courseId) {

		List<CourseRatingEntity> courseRatings = getDaoFactory().getCourseRatingDao().getByCourseId(em, courseId);
		for (CourseRatingEntity rating : courseRatings) {
			getDaoFactory().getCourseRatingDao().remove(em, rating.getId());
		}

		/**
		 * This is a bad implementation, but due to few time, it had to
		 * be implemented, it will be changed for the next release.
		 */
		List<CommentEntity> comments = getDaoFactory().getCommentDao().getByCourseId(em, courseId);

		CommentsService commentService = Config.getInstance().getServiceFactory().createCommentsService();
		for (CommentEntity comment : comments) {
			commentService.remove(em, comment.getId());
		}

		getDaoFactory().getCourseDao().remove(em, courseId);

	}

	@Override
	public CourseEntity voToEntity(EntityManager em, CourseVo vo) throws InvalidVoException, ExternalServiceConnectionException {
		validateVo(em, vo);
		CourseEntity course = new CourseEntity();
		course.setId(vo.getId());
		course.setPeriod(getDaoFactory().getPeriodDao().getById(em, vo.getPeriodId()));
		course.setProfessor(getDaoFactory().getProfessorDao().getById(em, vo.getProfessorId()));
		course.setSubject(getDaoFactory().getSubjectDao().getById(em, vo.getSubjectId()));
		course.setRatingAverage(vo.getRatingAverage());
		course.setRatingCount(vo.getRatingCount());
		return course;
	}

	@Override
	protected Dao<CourseEntity> getDao() {
		return getDaoFactory().getCourseDao();
	}
}
