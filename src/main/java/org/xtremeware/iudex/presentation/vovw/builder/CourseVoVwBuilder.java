package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.*;
import org.xtremeware.iudex.businesslogic.facade.CoursesFacade;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.facade.ProfessorsFacade;
import org.xtremeware.iudex.businesslogic.facade.SubjectsFacade;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelperImplementation;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.vo.*;

public class CourseVoVwBuilder {

	private static final String NO_PERIOD_STRING = "Sin periodo.";
	private static CourseVoVwBuilder instance;
	private FacadeFactory facadeFactory;

	private CourseVoVwBuilder(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	public static synchronized CourseVoVwBuilder getInstance() {
		if (instance == null) {
			instance = new CourseVoVwBuilder(Config.getInstance().getFacadeFactory());
		}
		return instance;
	}

	public CourseVoVwFull getCourseVoVwFull(long courseId) {
		SubjectsFacade subjectFacade = facadeFactory.getSubjectsFacade();
		CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
		SubjectVoVwSmall subject = new SubjectVoVwSmall(course.getSubjectVo().getId(),
				course.getSubjectVo().getName(),
				course.getSubjectVo().getCode(), subjectFacade.getSubjectsRatingSummary(course.getSubjectVo().getId()));
		ProfessorVoVwFull professor = ProfessorVoVwBuilder.getInstance().getProfessorFull(course.getProfessorVo());
		PeriodVo period = facadeFactory.getPeriodsFacade().getPeriod(course.getVo().getPeriodId());
		String periodString;
		if (period == null) {
			periodString = NO_PERIOD_STRING;
		} else {
			periodString = String.valueOf(period.getYear()) + " - " + String.valueOf(period.getSemester());
		}
		return new CourseVoVwFull(course.getVo(), subject, professor, periodString);
	}

	public List<CourseVoVwFull> getSearchResults(String query) {
		CoursesFacade coursesFacade = facadeFactory.getCoursesFacade();

		Date beforeSearch = new Date();
		List<Long> search = coursesFacade.search(query);
		Date afterSearch = new Date();
		Config.getInstance().getServiceFactory().getLogService().info("Searching for " + query + " took : " + String.valueOf(afterSearch.getTime() - beforeSearch.getTime()));
		ArrayList<CourseVoVwFull> results = new ArrayList<CourseVoVwFull>(search.size());
		HashMap<Long, ProfessorVoVwFull> professorsVo = new HashMap<Long, ProfessorVoVwFull>();
		HashMap<Long, SubjectVoVwSmall> subjectsVo = new HashMap<Long, SubjectVoVwSmall>();
		HashMap<Long, String> periods = new HashMap<Long, String>();

		ProfessorVoVwFull professor;
		SubjectVoVwSmall subject;
		for (Long courseId : search) {
			CourseVoFull course = coursesFacade.getCourse(courseId);
			Long professorId = course.getProfessorVo().getId();
			Long subjectId = course.getSubjectVo().getId();
			String periodString = NO_PERIOD_STRING;
			//Loof for the VoVw instances of the professor of this course 
			if (!professorsVo.containsKey(professorId)) {
				//The instance of this professor doesn't exists, create it
				professorsVo.put(professorId, ProfessorVoVwBuilder.getInstance().getProfessorFull(professorId));
			}
			professor = professorsVo.get(professorId);
			//Loof for the VoVw instances of the subject of this course 
			if (subjectsVo.containsKey(subjectId)) {
				subject = subjectsVo.get(subjectId);
			} else {
				//The instance of this subject doesn't exists, create it
				SubjectVoFull subjectFull = facadeFactory.getSubjectsFacade().getSubject(subjectId);
				subject = new SubjectVoVwSmall(subjectId, subjectFull.getVo().getName(), subjectFull.getVo().getCode(), subjectFull.getRatingSummary());
				subjectsVo.put(subjectId, subject);
			}
			PeriodVo period = facadeFactory.getPeriodsFacade().getPeriod(course.getVo().getPeriodId());
			if (period != null) {
				if (!periods.containsKey(course.getVo().getPeriodId())){
					periods.put(course.getVo().getPeriodId(), String.valueOf(period.getYear()) + " - " + String.valueOf(period.getSemester()));
				}
				periodString = periods.get(course.getVo().getPeriodId());
			}
			//Now that you have the correct instances create the specific course VoVw
			results.add(new CourseVoVwFull(course.getVo(), subject, professor,periodString));
		}

		return results;


	}
}
