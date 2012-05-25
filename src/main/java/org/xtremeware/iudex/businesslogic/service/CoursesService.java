package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.CoursesRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.CourseVo;

public class CoursesService extends CrudService<CourseVo, CourseEntity> {

	public CoursesService(AbstractDaoFactory daoFactory) {
		super(daoFactory,
				new SimpleCreate<CourseEntity>(daoFactory.getCourseDao()),
				new SimpleRead<CourseEntity>(daoFactory.getCourseDao()),
				new SimpleUpdate<CourseEntity>(daoFactory.getCourseDao()),
				new CoursesRemove(daoFactory));
	}

	@Override
	public void validateVo(EntityManager em, CourseVo course) throws
			MultipleMessagesException, DataBaseException {
		MultipleMessagesException multipleMessageException =
				new MultipleMessagesException();
		if (course == null) {
			multipleMessageException.addMessage("course.null");
			throw multipleMessageException;
		}
		if (course.getPeriodId() == null) {
			multipleMessageException.addMessage(
					"course.periodId.null");
		} else if (getDaoFactory().getPeriodDao().getById(em,
				course.getPeriodId()) == null) {
			multipleMessageException.addMessage(
					"course.periodId.notFound");
		}
		if (course.getProfessorId() == null) {
			multipleMessageException.addMessage(
					"course.professorId.null");
		} else if (getDaoFactory().getProfessorDao().getById(em, course.getProfessorId()) == null) {
			multipleMessageException.addMessage(
					"course.professorId.notFound");
		}
		if (course.getSubjectId() == null) {
			multipleMessageException.addMessage(
					"course.subjectId.null");
		} else if (getDaoFactory().getSubjectDao().getById(em, course.getSubjectId()) == null) {
			multipleMessageException.addMessage("course.subjectId.notFound");
		}
		if ((course.getRatingCount() == 0 && course.getRatingAverage() != 0)) {
			multipleMessageException.addMessage(
					"course.rating.invalid");
		}
		if (course.getRatingCount() < 0) {
			multipleMessageException.addMessage(
					"course.rating.invalidCount");
		}
		if (course.getRatingAverage() < 0) {
			multipleMessageException.addMessage(
					"course.rating.invalidAverage");
		}

		if (!multipleMessageException.getMessages().isEmpty()) {
			throw multipleMessageException;
		}
	}

	public List<CourseVo> getByProfessorId(EntityManager em, long professorId)
			throws DataBaseException {
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().getByProfessorId(em,
				professorId)) {
			list.add(c.toVo());
		}
		return list;
	}

	public List<CourseVo> getBySubjectId(EntityManager em, long subjectId)
			throws DataBaseException {
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().getBySubjectId(em,
				subjectId)) {
			list.add(c.toVo());
		}
		return list;
	}

	public List<CourseVo> getSimilarCourses(EntityManager em,
			String professorName, String subjectName, Long preiodId)
			throws ExternalServiceConnectionException, DataBaseException {
		professorName = SecurityHelper.sanitizeHTML(professorName);
		subjectName = SecurityHelper.sanitizeHTML(subjectName);
		List<CourseVo> list = new ArrayList<CourseVo>();
		for (CourseEntity c : getDaoFactory().getCourseDao().
				getCoursesByProfessorNameLikeAndSubjectNameLike(em,
				professorName, subjectName, preiodId)) {
			list.add(c.toVo());
		}
		return list;
	}

	@Override
	public CourseEntity voToEntity(EntityManager em, CourseVo vo) throws
			ExternalServiceConnectionException, MultipleMessagesException,
			DataBaseException {
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
}
