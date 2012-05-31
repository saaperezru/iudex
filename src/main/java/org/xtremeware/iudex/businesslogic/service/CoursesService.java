package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CourseVo;

public class CoursesService extends CrudService<CourseVo, CourseEntity> {

    public CoursesService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {
        super(daoFactory, create, read, update, delete);

    }

    @Override
    protected void validateVoForCreation(EntityManager entityManager, CourseVo course) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (course == null) {
            multipleMessageException.addMessage("course.null");
            throw multipleMessageException;
        }
        if (course.getPeriodId() == null) {
            multipleMessageException.addMessage(
                    "course.periodId.null");
        } else if (getDaoFactory().getPeriodDao().read(entityManager,
                course.getPeriodId()) == null) {
            multipleMessageException.addMessage(
                    "course.periodId.notFound");
        }
        if (course.getProfessorId() == null) {
            multipleMessageException.addMessage(
                    "course.professorId.null");
        } else if (getDaoFactory().getProfessorDao().read(entityManager, course.getProfessorId()) == null) {
            multipleMessageException.addMessage(
                    "course.professorId.notFound");
        }
        if (course.getSubjectId() == null) {
            multipleMessageException.addMessage(
                    "course.subjectId.null");
        } else if (getDaoFactory().getSubjectDao().read(entityManager, course.getSubjectId()) == null) {
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

    @Override
    public void validateVoForUpdate(EntityManager entityManager, CourseVo courseVo)
            throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, courseVo);
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (courseVo.getId() == null) {
            multipleMessageException.addMessage("course.id.null");
            throw multipleMessageException;
        }
    }

    public List<CourseVo> getByProfessorId(EntityManager entityManager, long professorId)
            throws DataBaseException {

        List<CourseVo> list = new ArrayList<CourseVo>();
        List<CourseEntity> courseEntitys = getDaoFactory().getCourseDao().getByProfessorId(entityManager,
                professorId);
        for (CourseEntity courseEntity : courseEntitys) {
            list.add(courseEntity.toVo());
        }
        return list;
    }

    public List<CourseVo> getBySubjectId(EntityManager entityManager, long subjectId)
            throws DataBaseException {
        List<CourseVo> list = new ArrayList<CourseVo>();
        List<CourseEntity> courseEntitys = getDaoFactory().getCourseDao().getBySubjectId(entityManager,
                subjectId);
        for (CourseEntity courseEntity : courseEntitys) {
            list.add(courseEntity.toVo());
        }
        return list;
    }

    public List<CourseVo> getSimilarCourses(EntityManager entityManager,
            String professorName, String subjectName, Long preiodId)
            throws ExternalServiceConnectionException, DataBaseException {

        List<CourseEntity> coursesByProfessorNameLikeAndSubjectNameLike =
                getDaoFactory().getCourseDao().
                getCoursesByProfessorNameLikeAndSubjectNameLike(entityManager,
                SecurityHelper.sanitizeHTML(professorName),
                SecurityHelper.sanitizeHTML(subjectName), preiodId);

        List<CourseVo> list = new ArrayList<CourseVo>();

        for (CourseEntity courseEntity : coursesByProfessorNameLikeAndSubjectNameLike) {
            list.add(courseEntity.toVo());
        }
        return list;
    }

    @Override
    public CourseEntity voToEntity(EntityManager entityManager, CourseVo courseVo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {

        CourseEntity course = new CourseEntity();
        course.setId(courseVo.getId());
        course.setPeriod(getDaoFactory().getPeriodDao().read(entityManager, courseVo.getPeriodId()));
        course.setProfessor(getDaoFactory().getProfessorDao().read(entityManager, courseVo.getProfessorId()));
        course.setSubject(getDaoFactory().getSubjectDao().read(entityManager, courseVo.getSubjectId()));
        course.setRatingAverage(courseVo.getRatingAverage());
        course.setRatingCount(courseVo.getRatingCount());
        return course;
    }
}
