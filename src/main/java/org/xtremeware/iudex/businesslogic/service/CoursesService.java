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
            Create create, Read read, Update update, Remove remove) {
        super(daoFactory, create, read, update, remove);

    }

    @Override
    public void validateVoForCreation(EntityManager entityManager, CourseVo courseVo) throws
            MultipleMessagesException, DataBaseException {
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (courseVo == null) {
            multipleMessageException.addMessage("Null CourseVo");
            throw multipleMessageException;
        }
        if (courseVo.getPeriodId() == null) {
            multipleMessageException.addMessage(
                    "Long periodId in the provided CourseVo cannot be null");
        } else if (getDaoFactory().getPeriodDao().getById(entityManager,
                courseVo.getPeriodId()) == null) {
            multipleMessageException.addMessage(
                    "Long periodId in the provided CourseVo must correspond to"
                    + " an existing period entity in the database");
        }
        if (courseVo.getProfessorId() == null) {
            multipleMessageException.addMessage(
                    "Long professorId in the provided CourseVo cannot be null");
        } else if (getDaoFactory().getProfessorDao().getById(entityManager, courseVo.getProfessorId()) == null) {
            multipleMessageException.addMessage(
                    "Long professorId in the provided CourseVo must correspond "
                    + "to an existing professor entity in the database");
        }
        if (courseVo.getSubjectId() == null) {
            multipleMessageException.addMessage(
                    "Long subjectId in the provided CourseVo cannot be null");
        } else if (getDaoFactory().getSubjectDao().getById(entityManager, courseVo.getSubjectId()) == null) {
            multipleMessageException.addMessage(
                    "Long subjectId in the provided CourseVo must correspond to"
                    + " an existing subject entity in the database");
        }
        if (courseVo.getRatingCount() == null) {
            multipleMessageException.addMessage(
                    "Long ratingCount in the provided CourseVo cannot be null");
        } else if (courseVo.getRatingCount() < 0) {
            multipleMessageException.addMessage(
                    "Long ratingCount in the provided CourseVo must be greater than one");
        }
        if (courseVo.getRatingAverage() == null) {
            multipleMessageException.addMessage(
                    "Double ratingAverage in the provided CourseVo cannot be null");
        }
        if (courseVo.getRatingAverage() < 0) {
            multipleMessageException.addMessage(
                    "Double ratingAverage in the provided "
                    + "CourseVo must be greater than one");
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

    public List<CourseVo> getSimilarCourses(EntityManager em,
            String professorName, String subjectName, Long preiodId)
            throws ExternalServiceConnectionException, DataBaseException {

        professorName = SecurityHelper.sanitizeHTML(professorName);
        subjectName = SecurityHelper.sanitizeHTML(subjectName);
        List<CourseEntity> coursesByProfessorNameLikeAndSubjectNameLike = getDaoFactory().getCourseDao().
                getCoursesByProfessorNameLikeAndSubjectNameLike(em,
                professorName, subjectName, preiodId);

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
        course.setPeriod(getDaoFactory().getPeriodDao().getById(entityManager, courseVo.getPeriodId()));
        course.setProfessor(getDaoFactory().getProfessorDao().getById(entityManager, courseVo.getProfessorId()));
        course.setSubject(getDaoFactory().getSubjectDao().getById(entityManager, courseVo.getSubjectId()));
        course.setRatingAverage(courseVo.getRatingAverage());
        course.setRatingCount(courseVo.getRatingCount());
        return course;
    }
}
