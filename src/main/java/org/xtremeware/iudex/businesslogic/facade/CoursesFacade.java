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
		List<CourseVoFull> list = new ArrayList<CourseVoFull>();
		if (query == null) {
			return null;
		}
		if (!query.isEmpty()) {

			Set<Long> coursesIds = new HashSet<Long>();
			HashMap<Long, ProfessorVoSmall> professorsVos = new HashMap<Long, ProfessorVoSmall>();
			HashMap<Long, SubjectVoSmall> subjectsVos = new HashMap<Long, SubjectVoSmall>();
			try {
				entityManager = getEntityManagerFactory().createEntityManager();

				List<ProfessorVo> professors = getServiceFactory().getProfessorsService().getByNameLike(entityManager, query);
				for (ProfessorVo professorVo : professors) {
					if (!professorsVos.containsKey(professorVo.getId())) {
						RatingSummaryVo rating = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorVo.getId());
						professorsVos.put(professorVo.getId(), new ProfessorVoSmall(professorVo, rating));

						List<SubjectVo> professorsSubjects = getServiceFactory().getSubjectsService().getByProfessorId(entityManager, professorVo.getId());
						for (SubjectVo subjectVo : professorsSubjects) {
							if (!subjectsVos.containsKey(subjectVo.getId())) {
								rating = getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
								subjectsVos.put(subjectVo.getId(), new SubjectVoSmall(subjectVo, rating));
							}
						}
					}
				}
				List<SubjectVo> subjects = getServiceFactory().getSubjectsService().getByNameLike(entityManager, query);
				for (SubjectVo subjectVo : subjects) {
					if (!subjectsVos.containsKey(subjectVo.getId())) {
						RatingSummaryVo rating = getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectVo.getId());
						subjectsVos.put(subjectVo.getId(), new SubjectVoSmall(subjectVo, rating));

						List<ProfessorVo> subjectsProfessors = getServiceFactory().getProfessorsService().getBySubjectId(entityManager, subjectVo.getId());
						for (ProfessorVo professorVo : subjectsProfessors) {
							if (!professorsVos.containsKey(professorVo.getId())) {
								rating = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorVo.getId());
								professorsVos.put(professorVo.getId(), new ProfessorVoSmall(professorVo, rating));
							}

						}
					}
				}


				for (ProfessorVo professorVo : professors) {
					for (CourseVo courseVo : getServiceFactory().createCoursesService().getByProfessorId(entityManager, professorVo.getId())) {
						//If this course is still not in the set of results, add it.
						if (!coursesIds.contains(courseVo.getId())) {
							coursesIds.add(courseVo.getId());
							list.add(new CourseVoFull(courseVo, subjectsVos.get(courseVo.getSubjectId()), professorsVos.get(courseVo.getProfessorId())));
						}
					}
				}
				for (SubjectVo subjectVo : subjects) {
					for (CourseVo courseVo : getServiceFactory().createCoursesService().getBySubjectId(entityManager, subjectVo.getId())) {
						//If this course is still not in the set of results, add it.
						if (!coursesIds.contains(courseVo.getId())) {
							list.add(new CourseVoFull(courseVo, subjectsVos.get(courseVo.getSubjectId()), professorsVos.get(courseVo.getProfessorId())));
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
			//TODO: The received summaries could be NULL. It is still pending to manage this case sucessfully
			SubjectVoSmall subjectVoSmall = new SubjectVoSmall(subjectVo, getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectVo.getId()));
			ProfessorVoSmall professorVoSmall = new ProfessorVoSmall(professorVo, getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorVo.getId()));

			courseVoFull = new CourseVoFull(vo, subjectVoSmall, professorVoSmall);

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

	/**
	 *
	 * @param professorId
	 * @return If there is no professor associated with the provided id an empty
	 * list will be returned
	 */
	public List<CourseVoFull> getByProfessorId(long professorId) {
		EntityManager em = null;
		List<CourseVoFull> list = new ArrayList<CourseVoFull>();
		HashMap<Long, SubjectVoSmall> subjectsVoVws = new HashMap<Long, SubjectVoSmall>();
		try {
			em = getEntityManagerFactory().createEntityManager();
			List<CourseVo> courses = getServiceFactory().createCoursesService().getByProfessorId(em, professorId);
			RatingSummaryVo subjectRating = getServiceFactory().getProfessorRatingsService().getSummary(em, professorId);
			ProfessorVo s = getServiceFactory().getProfessorsService().getById(em, professorId);
			ProfessorVoSmall professor = new ProfessorVoSmall(s.getId(), s.getFirstName() + " " + s.getLastName(), subjectRating);
			for (CourseVo c : courses) {
				if (!subjectsVoVws.containsKey(c.getSubjectId())) {
					RatingSummaryVo rating = getServiceFactory().getSubjectRatingsService().getSummary(em, c.getSubjectId());
					SubjectVo p = getServiceFactory().getSubjectsService().getById(em, c.getSubjectId());
					subjectsVoVws.put(p.getId(), new SubjectVoSmall(p, rating));
				}
				list.add(new CourseVoFull(c, subjectsVoVws.get(c.getSubjectId()), professor));
			}
		} catch (Exception e) {
			getServiceFactory().getLogService().error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			FacadesHelper.closeEntityManager(em);
		}
		return list;
	}
}
