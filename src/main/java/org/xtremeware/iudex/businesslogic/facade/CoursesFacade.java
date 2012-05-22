package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.VoVwFactory;
import org.xtremeware.iudex.vo.*;

public class CoursesFacade extends AbstractFacade {

    public CoursesFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
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
    public List<CourseVoVwFull> search(String query) {
        EntityManager entityManager = null;
        List<CourseVoVwFull> list = new ArrayList<CourseVoVwFull>();
        if (query == null) {
            return null;
        }
        if (!query.isEmpty()) {

            Set<Long> coursesIds = new HashSet<Long>();
            HashMap<Long, ProfessorVoVwSmall> professorsVoVws = new HashMap<Long, ProfessorVoVwSmall>();
            HashMap<Long, SubjectVoVwSmall> subjectsVoVws = new HashMap<Long, SubjectVoVwSmall>();
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<ProfessorVo> professors = getServiceFactory().createProfessorsService().getByNameLike(entityManager, query);
                for (ProfessorVo professorVo : professors) {
                    if (!professorsVoVws.containsKey(professorVo.getId())) {
                        RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(entityManager, professorVo.getId());
                        professorsVoVws.put(professorVo.getId(), VoVwFactory.getProfessorVoVwSmall(professorVo, rating));
                    }

                    List<SubjectVo> professorsSubjects = getServiceFactory().createSubjectsService().getByProfessorId(entityManager, professorVo.getId());
                    for (SubjectVo subjectVo : professorsSubjects) {
                        if (!subjectsVoVws.containsKey(subjectVo.getId())) {
                            RatingSummaryVo rating = getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
                            subjectsVoVws.put(subjectVo.getId(), VoVwFactory.getSubjectVoVwSmall(subjectVo, rating));
                        }
                    }
                }
                List<SubjectVo> subjects = getServiceFactory().createSubjectsService().getByNameLike(entityManager, query);
                for (SubjectVo subjectVo : subjects) {
                    if (!subjectsVoVws.containsKey(subjectVo.getId())) {
                        RatingSummaryVo rating = getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
                        subjectsVoVws.put(subjectVo.getId(), VoVwFactory.getSubjectVoVwSmall(subjectVo, rating));
                    }

                    List<ProfessorVo> subjectsProfessors = getServiceFactory().createProfessorsService().getBySubjectId(entityManager, subjectVo.getId());
                    for (ProfessorVo professorVo : subjectsProfessors) {
                        if (!professorsVoVws.containsKey(professorVo.getId())) {
                            RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(entityManager, professorVo.getId());
                            professorsVoVws.put(professorVo.getId(), VoVwFactory.getProfessorVoVwSmall(professorVo, rating));
                        }

                    }
                }


                for (ProfessorVo professorVo : professors) {
                    for (CourseVo courseVo : getServiceFactory().createCoursesService().getByProfessorId(entityManager, professorVo.getId())) {
                        //If this course is still not in the set of results, add it.
                        if (!coursesIds.contains(courseVo.getId())) {
                            list.add(VoVwFactory.getCourseVoVwFull(courseVo, subjectsVoVws.get(courseVo.getSubjectId()), professorsVoVws.get(courseVo.getProfessorId())));
                        }
                    }
                }
                for (SubjectVo subjectVo : subjects) {
                    for (CourseVo courseVo : getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectVo.getId())) {
                        //If this course is still not in the set of results, add it.
                        if (!coursesIds.contains(courseVo.getId())) {
                            list.add(VoVwFactory.getCourseVoVwFull(courseVo, subjectsVoVws.get(courseVo.getSubjectId()), professorsVoVws.get(courseVo.getProfessorId())));
                        }
                    }

                }

            } catch (Exception e) {
                getServiceFactory().createLogService().error(e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                FacadesHelper.closeEntityManager(entityManager);
            }
        }
        return list;
    }

    public CourseVo addCourse(long professorId, long subjectId, long periodId) throws MultipleMessagesException, Exception {
        CourseVo createdVo = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            CourseVo vo = new CourseVo();
            vo.setPeriodId(periodId);
            vo.setProfessorId(professorId);
            vo.setSubjectId(subjectId);
            vo.setRatingCount(0L);
            vo.setRatingAverage(0.0);
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createCoursesService().create(em, vo);
            tx.commit();
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return createdVo;
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
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }

    public CourseVoVwFull getCourse(long courseId) {
        EntityManager em = null;
        CourseVoVwFull voVw = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            CourseVo vo = getServiceFactory().createCoursesService().getById(em, courseId);
            if (vo == null) {
                return null;
            }
            SubjectVo subjectVo = getServiceFactory().createSubjectsService().getById(em, vo.getSubjectId());
            ProfessorVo professorVo = getServiceFactory().createProfessorsService().getById(em, vo.getProfessorId());
            //TODO: The received summaries could be NULL. It is still pending to manage this case sucessfully
            SubjectVoVwSmall subjectVoVwSmall = VoVwFactory.getSubjectVoVwSmall(subjectVo, getServiceFactory().createSubjectRatingsService().getSummary(em, subjectVo.getId()));
            ProfessorVoVwSmall professorVoVwSmall = VoVwFactory.getProfessorVoVwSmall(professorVo, getServiceFactory().createProfessorRatingsService().getSummary(em, professorVo.getId()));

            voVw = VoVwFactory.getCourseVoVwFull(vo, subjectVoVwSmall, professorVoVwSmall);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return voVw;
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
            rating = getServiceFactory().createCourseRatingsService().getByCourseIdAndUserId(em, courseId, userId);
            //If there is no existing record in the database, create it
            if (rating == null) {
                rating = getServiceFactory().createCourseRatingsService().create(em, vo);
            } else {
                //Otherwise update the existing one
                //But first verify bussines rules
                getServiceFactory().createCourseRatingsService().validateVoForUpdate(em, vo);
                rating.setValue(value);
            }
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return rating;

    }

    public CourseRatingVo getCourseRatingByUserId(long courseId, long userId) {
        EntityManager em = null;
        CourseRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createCourseRatingsService().getByCourseIdAndUserId(em, courseId, userId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return rating;
    }

    public List<CourseVoVwFull> getSimilarCourses(String professorName, String subjectName, Long periodId) {
        EntityManager em = null;
        List<CourseVoVwFull> list = new ArrayList<CourseVoVwFull>();
        HashMap<Long, ProfessorVoVwSmall> professorsVoVws = new HashMap<Long, ProfessorVoVwSmall>();
        HashMap<Long, SubjectVoVwSmall> subjectsVoVws = new HashMap<Long, SubjectVoVwSmall>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            List<CourseVo> similarCourses = getServiceFactory().createCoursesService().getSimilarCourses(em, professorName, subjectName, periodId);
            for (CourseVo courseVo : similarCourses) {
                if (!professorsVoVws.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().createProfessorsService().getById(em, courseVo.getProfessorId());
                    professorsVoVws.put(professorVo.getId(), VoVwFactory.getProfessorVoVwSmall(professorVo, getServiceFactory().createProfessorRatingsService().getSummary(em, courseVo.getProfessorId())));
                }
                if (!subjectsVoVws.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().createSubjectsService().getById(em, courseVo.getSubjectId());
                    subjectsVoVws.put(subjectVo.getId(), VoVwFactory.getSubjectVoVwSmall(subjectVo, getServiceFactory().createSubjectRatingsService().getSummary(em, courseVo.getSubjectId())));
                }
                list.add(VoVwFactory.getCourseVoVwFull(courseVo, subjectsVoVws.get(courseVo.getSubjectId()), professorsVoVws.get(courseVo.getProfessorId())));

            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }

    public List<CourseVoVwFull> getBySubjectId(long subjectId) {
        EntityManager entityManager = null;
        List<CourseVoVwFull> list = new ArrayList<CourseVoVwFull>();
        HashMap<Long, ProfessorVoVwSmall> professorsVoVws = new HashMap<Long, ProfessorVoVwSmall>();
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<CourseVo> courses = getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectId);
            SubjectVoVwSmall subject = VoVwFactory.getSubjectVoVwSmall(getServiceFactory().createSubjectsService().getById(entityManager, subjectId), getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectId));
            for (CourseVo courseVo : courses) {
                if (!professorsVoVws.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo p = getServiceFactory().createProfessorsService().getById(entityManager, courseVo.getProfessorId());
                    professorsVoVws.put(p.getId(), VoVwFactory.getProfessorVoVwSmall(p, getServiceFactory().createProfessorRatingsService().getSummary(entityManager, courseVo.getProfessorId())));
                }
                list.add(VoVwFactory.getCourseVoVwFull(courseVo, subject, professorsVoVws.get(courseVo.getProfessorId())));
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return list;
    }
}
