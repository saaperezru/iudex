package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.xtremeware.iudex.businesslogic.facade.CoursesFacade;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.facade.ProfessorsFacade;
import org.xtremeware.iudex.businesslogic.facade.SubjectsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.vo.CourseVoFull;
import org.xtremeware.iudex.vo.ProfessorVoFull;
import org.xtremeware.iudex.vo.SubjectVoFull;

public class CourseVoVwBuilder {

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
		return new CourseVoVwFull(course.getVo(), subject, professor);
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

		ProfessorVoVwFull professor;
		SubjectVoVwSmall subject;
		for (Long courseId : search) {
			CourseVoFull course = coursesFacade.getCourse(courseId);
			Long professorId = course.getProfessorVo().getId();
			Long subjectId = course.getSubjectVo().getId();
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
			//Now that you have the correct instances create the specific course VoVw
			results.add(new CourseVoVwFull(course.getVo(), subject, professor));
		}

		return results;


	}
}
