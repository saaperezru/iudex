package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.VoVwFactory;
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
                List<ProfessorVo> professors = getServiceFactory().getProfessorsService().getByNameLike(entityManager, query);
                for (ProfessorVo professorVo : professors) {
                    if (!professorsVoVws.containsKey(professorVo.getId())) {
                        RatingSummaryVo rating = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorVo.getId());
                        professorsVoVws.put(professorVo.getId(), VoVwFactory.getProfessorVoVwSmall(professorVo, rating));
                    }

                    List<SubjectVo> professorsSubjects = getServiceFactory().getSubjectsService().getByProfessorId(entityManager, professorVo.getId());
                    for (SubjectVo subjectVo : professorsSubjects) {
                        if (!subjectsVoVws.containsKey(subjectVo.getId())) {
                            RatingSummaryVo rating = getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
                            subjectsVoVws.put(subjectVo.getId(), VoVwFactory.getSubjectVoVwSmall(subjectVo, rating));
                        }
                    }
                }
                List<SubjectVo> subjects = getServiceFactory().getSubjectsService().getByNameLike(entityManager, query);
                for (SubjectVo subjectVo : subjects) {
                    if (!subjectsVoVws.containsKey(subjectVo.getId())) {
                        RatingSummaryVo rating = getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
                        subjectsVoVws.put(subjectVo.getId(), VoVwFactory.getSubjectVoVwSmall(subjectVo, rating));
                    }

                    List<ProfessorVo> subjectsProfessors = getServiceFactory().getProfessorsService().getBySubjectId(entityManager, subjectVo.getId());
                    for (ProfessorVo professorVo : subjectsProfessors) {
                        if (!professorsVoVws.containsKey(professorVo.getId())) {
                            RatingSummaryVo rating = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorVo.getId());
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
                getServiceFactory().getLogService().error(e.getMessage(), e);
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
            getServiceFactory().getLogService().error(e.getMessage(), e);
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
            getServiceFactory().getLogService().error(e.getMessage(), e);
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
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().getById(em, vo.getSubjectId());
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().getById(em, vo.getProfessorId());
            //TODO: The received summaries could be NULL. It is still pending to manage this case sucessfully
            SubjectVoVwSmall subjectVoVwSmall = VoVwFactory.getSubjectVoVwSmall(subjectVo, getServiceFactory().getSubjectRatingsService().getSummary(em, subjectVo.getId()));
            ProfessorVoVwSmall professorVoVwSmall = VoVwFactory.getProfessorVoVwSmall(professorVo, getServiceFactory().getProfessorRatingsService().getSummary(em, professorVo.getId()));

            voVw = VoVwFactory.getCourseVoVwFull(vo, subjectVoVwSmall, professorVoVwSmall);

        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
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
            rating = getServiceFactory().getCourseRatingsService().getByCourseIdAndUserId(em, courseId, userId);
            //If there is no existing record in the database, create it
            if (rating == null) {
                rating = getServiceFactory().getCourseRatingsService().create(em, vo);
            } else {
                //Otherwise update the existing one
                //But first verify bussines rules
                getServiceFactory().getCourseRatingsService().validateVoForUpdate(em, vo);
                rating.setValue(value);
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
        HashMap<Long, ProfessorVoSmall> professorsVos = new HashMap<Long, ProfessorVoSmall>();
        HashMap<Long, SubjectVoSmall> subjectsVos = new HashMap<Long, SubjectVoSmall>();
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<CourseVo> similarCourses = getServiceFactory().
                    createCoursesService().getSimilarCourses(entityManager, professorName, subjectName, periodId);
            for (CourseVo courseVo : similarCourses) {
                if (!professorsVos.containsKey(courseVo.getProfessorId())) {
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().getById(entityManager, courseVo.getProfessorId());
                    professorsVos.put(professorVo.getId(), new ProfessorVoSmall(professorVo, getServiceFactory().getProfessorRatingsService().getSummary(entityManager, courseVo.getProfessorId())));
                }
                if (!subjectsVos.containsKey(courseVo.getSubjectId())) {
                    SubjectVo subjectVo = getServiceFactory().
                            getSubjectsService().getById(entityManager, courseVo.getSubjectId());
                    subjectsVos.put(subjectVo.getId(), new SubjectVoSmall(subjectVo, getServiceFactory().getSubjectRatingsService().getSummary(entityManager, courseVo.getSubjectId())));
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
        HashMap<Long, ProfessorVoSmall> professorsidAndSmallRepresentation = new HashMap<Long, ProfessorVoSmall>();
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            
            List<CourseVo> courseVos = getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectId);
            
            SubjectVo subjectVo = getServiceFactory().getSubjectsService().getById(entityManager, subjectId);
            RatingSummaryVo SubjectSummary = getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectId);
            SubjectVoSmall subject = new SubjectVoSmall(subjectVo, SubjectSummary);
            
            for (CourseVo courseVo : courseVos) {
                if (!professorsidAndSmallRepresentation.containsKey(courseVo.getProfessorId())) {
                    
                    ProfessorVo professorVo = getServiceFactory().
                            getProfessorsService().getById(entityManager, courseVo.getProfessorId());
                    RatingSummaryVo professorSumary = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, courseVo.getProfessorId());
                    professorsidAndSmallRepresentation.put(professorVo.getId(), new ProfessorVoSmall(professorVo, professorSumary));
                }
                courseVoFulls.add(
                        new CourseVoFull(
                        courseVo, subject, professorsidAndSmallRepresentation.get(courseVo.getProfessorId())));
            }

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return courseVoFulls;
    }
}
