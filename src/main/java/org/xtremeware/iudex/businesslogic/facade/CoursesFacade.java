package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
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
    public List<CourseVoFull> search(String query) {
        EntityManager entityManager = null;

        List<CourseVoFull> listOfFoundCourses = new ArrayList<CourseVoFull>();

        if (query != null) {
            if (!query.isEmpty()) {

                Set<Long> coursesIds = new HashSet<Long>();
                HashMap<Long, ProfessorVo> professorsVo = new HashMap<Long, ProfessorVo>();
                HashMap<Long, SubjectVo> subjectsVo = new HashMap<Long, SubjectVo>();
                try {
                    entityManager = getEntityManagerFactory().createEntityManager();

                    List<ProfessorVo> professors = getServiceFactory().getProfessorsService().getByNameLike(entityManager, query);
                    for (ProfessorVo professorVo : professors) {
                        if (!professorsVo.containsKey(professorVo.getId())) {
                            professorsVo.put(professorVo.getId(), professorVo);

                            List<SubjectVo> professorsSubjects = getServiceFactory().getSubjectsService().getByProfessorId(entityManager, professorVo.getId());
                            for (SubjectVo subjectVo : professorsSubjects) {
                                if (!subjectsVo.containsKey(subjectVo.getId())) {
                                    subjectsVo.put(subjectVo.getId(), subjectVo);
                                }
                            }
                        }
                    }

                    List<SubjectVo> subjects = getServiceFactory().getSubjectsService().getByNameLike(entityManager, query);
                    for (SubjectVo subjectVo : subjects) {
                        if (!subjectsVo.containsKey(subjectVo.getId())) {
                            subjectsVo.put(subjectVo.getId(), subjectVo);

                            List<ProfessorVo> subjectsProfessors = getServiceFactory().getProfessorsService().getBySubjectId(entityManager, subjectVo.getId());
                            for (ProfessorVo professorVo : subjectsProfessors) {
                                if (!professorsVo.containsKey(professorVo.getId())) {
                                    professorsVo.put(professorVo.getId(), professorVo);
                                }

                            }
                        }
                    }


                    for (ProfessorVo professorVo : professors) {
                        for (CourseVo courseVo : getServiceFactory().createCoursesService().getByProfessorId(entityManager, professorVo.getId())) {
                            //If this course is still not in the set of results, add it.
                            if (!coursesIds.contains(courseVo.getId())) {
                                coursesIds.add(courseVo.getId());
                                listOfFoundCourses.add(new CourseVoFull(courseVo, subjectsVo.get(courseVo.getSubjectId()), professorsVo.get(courseVo.getProfessorId())));
                            }
                        }
                    }
                    for (SubjectVo subjectVo : subjects) {
                        for (CourseVo courseVo : getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectVo.getId())) {
                            //If this course is still not in the set of results, add it.
                            if (!coursesIds.contains(courseVo.getId())) {
                                listOfFoundCourses.add(new CourseVoFull(courseVo, subjectsVo.get(courseVo.getSubjectId()), professorsVo.get(courseVo.getProfessorId())));
                            }
                        }

                    }

                } catch (Exception e) {
                    getServiceFactory().getLogService().error(e.getMessage(), e);
                    throw new RuntimeException(e);
                } finally {
                    FacadesHelper.closeEntityManager(entityManager);
                }
            }
        }
        return listOfFoundCourses;
    }

    public CourseVo addCourse(long professorId, long subjectId, long periodId)
            throws MultipleMessagesException, DuplicityException {
        CourseVo courseVo = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            courseVo = new CourseVo();
            courseVo.setPeriodId(periodId);
            courseVo.setProfessorId(professorId);
            courseVo.setSubjectId(subjectId);
            courseVo.setRatingCount(0L);
            courseVo.setRatingAverage(0.0);
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            courseVo = getServiceFactory().createCoursesService().create(em, courseVo);
            tx.commit();
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return courseVo;
    }

    public void removeCourse(long courseId) throws Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createCoursesService().remove(em, courseId);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }

    public CourseVoFull getCourse(long courseId) {
        EntityManager entityManager = null;
        CourseVoFull courseVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            CourseVo vo = getServiceFactory().createCoursesService().getById(entityManager, courseId);
            if (vo == null) {
                return null;
            }
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().getById(entityManager, vo.getSubjectId());
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().getById(entityManager, vo.getProfessorId());

            courseVoFull = new CourseVoFull(vo, subjectVo, professorVo);

        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVoFull;
    }

    public CourseRatingVo rateCourse(long courseId, long userId, float value) throws MultipleMessagesException, Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        CourseRatingVo rating = null;
        try {
            CourseRatingVo vo = new CourseRatingVo();
            vo.setCourseId(courseId);
            vo.setUserId(userId);
            vo.setValue(value);

            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            rating = getServiceFactory().getCourseRatingsService().getByCourseIdAndUserId(em, courseId, userId);
            //If there is no existing record in the database, create it
            if (rating == null) {
                rating = getServiceFactory().getCourseRatingsService().create(em, vo);
            } else {
                //Otherwise update the existing one
                //But first verify bussines rules

                rating.setValue(value);
                getServiceFactory().getCourseRatingsService().update(em, rating);
            }
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
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
                    createCoursesService().getSimilarCourses(entityManager, professorName, subjectName, periodId);
            for (CourseVo courseVo : similarCourses) {
                if (!professorsVos.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().getById(entityManager, courseVo.getProfessorId());
                    professorsVos.put(professorVo.getId(), professorVo);
                }
                if (!subjectsVos.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().
                            getSubjectsService().getById(entityManager, courseVo.getSubjectId());
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
            List<CourseVo> courseVos = getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectId);
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().getById(entityManager, subjectId);
            for (CourseVo courseVo : courseVos) {
                if (!professorsVo.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().getById(entityManager, courseVo.getProfessorId());
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
            List<CourseVo> courses = getServiceFactory().createCoursesService().getByProfessorId(entityManager, professorId);
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().getById(entityManager, professorId);
            for (CourseVo courseVo : courses) {
                if (!subjectsVo.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().getSubjectsService().getById(entityManager, courseVo.getSubjectId());
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
