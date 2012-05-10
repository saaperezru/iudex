package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
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
        EntityManager em = null;
        List<CourseVoVwFull> list = new ArrayList<CourseVoVwFull>();
        if (!query.isEmpty()) {

            Set<Long> coursesIds = new HashSet<Long>();
            HashMap<Long, ProfessorVoVwSmall> professorsVoVws = new HashMap<Long, ProfessorVoVwSmall>();
            HashMap<Long, SubjectVoVwSmall> subjectsVoVws = new HashMap<Long, SubjectVoVwSmall>();
            try {
                em = getEntityManagerFactory().createEntityManager();
                List<ProfessorVo> professors = getServiceFactory().createProfessorsService().getByNameLike(em, query);
                for (ProfessorVo p : professors) {
                    if (!professorsVoVws.containsKey(p.getId())) {
                        RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(em, p.getId());
                        professorsVoVws.put(p.getId(), new ProfessorVoVwSmall(p.getId(), p.getFirstName() + " " + p.getLastName(), rating));
                    }

                    List<SubjectVo> professorsSubjects = getServiceFactory().createSubjectsService().getByProfessorId(em, p.getId());
                    for (SubjectVo s : professorsSubjects) {
                        if (!subjectsVoVws.containsKey(s.getId())) {
                            RatingSummaryVo rating = getServiceFactory().createSubjectRatingsService().getSummary(em, s.getId());
                            subjectsVoVws.put(s.getId(), new SubjectVoVwSmall(s.getId(), s.getName(), rating));
                        }
                    }
                }
                List<SubjectVo> subjects = getServiceFactory().createSubjectsService().getByNameLike(em, query);
                for (SubjectVo s : subjects) {
                    if (!subjectsVoVws.containsKey(s.getId())) {
                        RatingSummaryVo rating = getServiceFactory().createSubjectRatingsService().getSummary(em, s.getId());
                        subjectsVoVws.put(s.getId(), new SubjectVoVwSmall(s.getId(), s.getName(), rating));
                    }

                    List<ProfessorVo> subjectsProfessors = getServiceFactory().createProfessorsService().getBySubjectId(em, s.getId());
                    for (ProfessorVo p : subjectsProfessors) {
                        if (!professorsVoVws.containsKey(p.getId())) {
                            RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(em, p.getId());
                            professorsVoVws.put(p.getId(), new ProfessorVoVwSmall(p.getId(), p.getFirstName() + " " + p.getLastName(), rating));
                        }

                    }
                }


                for (ProfessorVo p : professors) {
                    for (CourseVo c : getServiceFactory().createCoursesService().getByProfessorId(em, p.getId())) {
                        //If this course is still not in the set of results, add it.
                        if (!coursesIds.contains(c.getId())) {
                            list.add(new CourseVoVwFull(c, subjectsVoVws.get(c.getSubjectId()), professorsVoVws.get(c.getProfessorId())));
                        }
                    }
                }
                for (SubjectVo s : subjects) {
                    for (CourseVo c : getServiceFactory().createCoursesService().getBySubjectId(em, s.getId())) {
                        //If this course is still not in the set of results, add it.
                        if (!coursesIds.contains(c.getId())) {
                            list.add(new CourseVoVwFull(c, subjectsVoVws.get(c.getSubjectId()), professorsVoVws.get(c.getProfessorId())));
                        }
                    }

                }

            } catch (Exception e) {
                getServiceFactory().createLogService().error(e.getMessage(), e);
            } finally {
                if (em != null) {
                    em.clear();
                    em.close();
                }
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
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw  e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
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
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
    }

    public CourseVoVwFull getCourse(long courseId) throws Exception {
        EntityManager em = null;
        CourseVoVwFull voVw = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            CourseVo vo = getServiceFactory().createCoursesService().getById(em, courseId);
            if (vo == null) {
                return null;
            }
            SubjectVo subject = getServiceFactory().createSubjectsService().getById(em, vo.getSubjectId());
            ProfessorVo professor = getServiceFactory().createProfessorsService().getById(em, vo.getProfessorId());
            //TODO: The received summaries could be NULL. It is still pending to manage this case sucessfully
            SubjectVoVwSmall subjectSmall = new SubjectVoVwSmall(subject.getId(), subject.getName(), getServiceFactory().createSubjectRatingsService().getSummary(em, subject.getId()));
            ProfessorVoVwSmall professorSmall = new ProfessorVoVwSmall(professor.getId(), professor.getFirstName() + " " + professor.getLastName(), getServiceFactory().createProfessorRatingsService().getSummary(em, professor.getId()));

            voVw = new CourseVoVwFull(vo, subjectSmall, professorSmall);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
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
                getServiceFactory().createCourseRatingsService().validateVo(em, vo);
                rating.setValue(value);
            }
            tx.commit();

        } catch (MultipleMessagesException ex) {
            throw ex;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw  e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return rating;

    }

    public CourseRatingVo getCourseRatingByUserId(long courseId, long userId) throws Exception {
        EntityManager em = null;
        CourseRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createCourseRatingsService().getByCourseIdAndUserId(em, courseId, userId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
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
            for (CourseVo c : similarCourses) {
                if (!professorsVoVws.containsKey(c.getProfessorId())) {
                    RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(em, c.getProfessorId());
                    ProfessorVo p = getServiceFactory().createProfessorsService().getById(em, c.getProfessorId());
                    professorsVoVws.put(p.getId(), new ProfessorVoVwSmall(p.getId(), p.getFirstName() + " " + p.getLastName(), rating));
                }
                if (!subjectsVoVws.containsKey(c.getSubjectId())) {
                    RatingSummaryVo rating = getServiceFactory().createSubjectRatingsService().getSummary(em, c.getSubjectId());
                    SubjectVo s = getServiceFactory().createSubjectsService().getById(em, c.getSubjectId());
                    subjectsVoVws.put(s.getId(), new SubjectVoVwSmall(s.getId(), s.getName(), rating));
                }
                list.add(new CourseVoVwFull(c, subjectsVoVws.get(c.getSubjectId()), professorsVoVws.get(c.getProfessorId())));

            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return list;
    }

    public List<CourseVoVwFull> getBySubjectId(long subjectId) {
        EntityManager em = null;
        List<CourseVoVwFull> list = new ArrayList<CourseVoVwFull>();
        HashMap<Long, ProfessorVoVwSmall> professorsVoVws = new HashMap<Long, ProfessorVoVwSmall>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            List<CourseVo> courses = getServiceFactory().createCoursesService().getBySubjectId(em, subjectId);
            RatingSummaryVo subjectRating = getServiceFactory().createSubjectRatingsService().getSummary(em, subjectId);
            SubjectVo s = getServiceFactory().createSubjectsService().getById(em, subjectId);
            SubjectVoVwSmall subject = new SubjectVoVwSmall(s.getId(), s.getName(), subjectRating);
            for (CourseVo c : courses) {
                if (!professorsVoVws.containsKey(c.getProfessorId())) {
                    RatingSummaryVo rating = getServiceFactory().createProfessorRatingsService().getSummary(em, c.getProfessorId());
                    ProfessorVo p = getServiceFactory().createProfessorsService().getById(em, c.getProfessorId());
                    professorsVoVws.put(p.getId(), new ProfessorVoVwSmall(p.getId(), p.getFirstName() + " " + p.getLastName(), rating));
                }
                list.add(new CourseVoVwFull(c, subject, professorsVoVws.get(c.getProfessorId())));
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return list;
    }
}
