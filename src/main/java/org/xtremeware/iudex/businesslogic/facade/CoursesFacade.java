package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class CoursesFacade extends AbstractFacade {

    public CoursesFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    /**
     * Searches for courses by professor and subject name that contain the
     * string specified in the query parameter. If the query is empty or full of
     * spaces, the results will be empty. (Because of the overhead of bringing
     * ALL the information from database and translating it to the proper
     * format).
     *
     * @param query String to be searched among the professors and subjects
     * names.
     * @return A list of CourseVoVwFull with the information of the found
     * courses.
     */
    public List<Long> search(String query) {
        EntityManager entityManager = null;

        Set<Long> coursesIds = new HashSet<Long>();
        if (query != null && !query.isEmpty()) {


            try {
                entityManager = getEntityManagerFactory().createEntityManager();

                List<Long> professors = getServiceFactory().getProfessorsService().getByNameLike(entityManager, query);

                List<Long> subjects = getServiceFactory().getSubjectsService().getByNameLike(entityManager, query);

                for (Long professorVoId : professors) {
                    for (CourseVo courseVo : getServiceFactory().getCoursesService().getByProfessorId(entityManager, professorVoId)) {
                        coursesIds.add(courseVo.getId());
                    }
                }
                for (Long subjectVoId : subjects) {
                    for (CourseVo courseVo : getServiceFactory().getCoursesService().getBySubjectId(entityManager, subjectVoId)) {
                        coursesIds.add(courseVo.getId());
                    }

                }

            } catch (Exception e) {
                getServiceFactory().getLogService().error(e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                FacadesHelper.closeEntityManager(entityManager);
            }
        }
        return Arrays.asList(coursesIds.toArray(new Long[]{}));
    }

    public CourseVo createCourse(long professorId, long subjectId, long periodId)
            throws MultipleMessagesException, DuplicityException {
        CourseVo courseVo = null;
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            courseVo = new CourseVo();
            courseVo.setPeriodId(periodId);
            courseVo.setProfessorId(professorId);
            courseVo.setSubjectId(subjectId);
            courseVo.setRatingCount(0L);
            courseVo.setRatingAverage(0.0);
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            courseVo = getServiceFactory().getCoursesService().create(entityManager, courseVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVo;
    }

    public void deleteCourse(long courseId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getCoursesService().delete(entityManager, courseId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public CourseVoFull getCourse(long courseId) {
        EntityManager entityManager = null;
        CourseVoFull courseVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            CourseVo vo = getServiceFactory().getCoursesService().read(entityManager, courseId);
            if (vo == null) {
                return null;
            }
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().read(entityManager, vo.getSubjectId());
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().read(entityManager, vo.getProfessorId());

            courseVoFull = new CourseVoFull(vo, subjectVo, professorVo);

        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVoFull;
    }

    public CourseRatingVo rateCourse(long courseId, long userId, float value)
            throws MultipleMessagesException, DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        CourseRatingVo rating = null;
        try {
            CourseRatingVo vo = new CourseRatingVo();
            vo.setCourseId(courseId);
            vo.setUserId(userId);
            vo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            rating = getServiceFactory().getCourseRatingsService().create(entityManager, vo);

            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }

    public CourseRatingVo getCourseRatingByUserId(long courseId, long userId) {
        EntityManager entityManager = null;
        CourseRatingVo courseRatingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            courseRatingVo = getServiceFactory().getCourseRatingsService().getByCourseIdAndUserId(entityManager, courseId, userId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseRatingVo;
    }

    public List<CourseVoFull> getSimilarCourses(String professorName, String subjectName, Long periodId) {
        EntityManager entityManager = null;
        List<CourseVoFull> courseVoFulls = new ArrayList<CourseVoFull>();

        HashMap<Long, ProfessorVo> professorsVos = new HashMap<Long, ProfessorVo>();
        HashMap<Long, SubjectVo> subjectsVos = new HashMap<Long, SubjectVo>();

        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<CourseVo> similarCourses = getServiceFactory().
                    getCoursesService().getSimilarCourses(entityManager, professorName, subjectName, periodId);
            for (CourseVo courseVo : similarCourses) {
                if (!professorsVos.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().read(entityManager, courseVo.getProfessorId());
                    professorsVos.put(professorVo.getId(), professorVo);
                }
                if (!subjectsVos.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().
                            getSubjectsService().read(entityManager, courseVo.getSubjectId());
                    subjectsVos.put(subjectVo.getId(), subjectVo);
                }
                courseVoFulls.add(new CourseVoFull(courseVo, subjectsVos.get(courseVo.getSubjectId()), professorsVos.get(courseVo.getProfessorId())));
            }

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVoFulls;
    }

    public List<CourseVoFull> getBySubjectId(long subjectId) {
        EntityManager entityManager = null;
        List<CourseVoFull> courseVoFulls = new ArrayList<CourseVoFull>();
        HashMap<Long, ProfessorVo> professorsVo = new HashMap<Long, ProfessorVo>();
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<CourseVo> courseVos = getServiceFactory().getCoursesService().getBySubjectId(entityManager, subjectId);
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().read(entityManager, subjectId);
            for (CourseVo courseVo : courseVos) {
                if (!professorsVo.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().read(entityManager, courseVo.getProfessorId());
                    professorsVo.put(professorVo.getId(), professorVo);
                }
                courseVoFulls.add(
                        new CourseVoFull(
                        courseVo, subjectVo, professorsVo.get(courseVo.getProfessorId())));
            }
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVoFulls;
    }

    /**
     *
     * @param professorId
     * @return If there is no professor associated with the provided id an empty
     * list will be returned
     */
    public List<CourseVoFull> getByProfessorId(long professorId) {
        EntityManager entityManager = null;
        List<CourseVoFull> coursesList = new ArrayList<CourseVoFull>();
        HashMap<Long, SubjectVo> subjectsVo = new HashMap<Long, SubjectVo>();

        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<CourseVo> courses = getServiceFactory().getCoursesService().getByProfessorId(entityManager, professorId);
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().read(entityManager, professorId);
            for (CourseVo courseVo : courses) {
                if (!subjectsVo.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().getSubjectsService().read(entityManager, courseVo.getSubjectId());
                    subjectsVo.put(subjectVo.getId(), subjectVo);
                }
                coursesList.add(new CourseVoFull(courseVo, subjectsVo.get(courseVo.getSubjectId()), professorVo));
            }
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return coursesList;
    }
}
