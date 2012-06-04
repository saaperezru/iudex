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
	
	private static CourseVoVwBuilder instance;
	private FacadeFactory facadeFactory;
	private static HashMap<Long, ProfessorVoVwFull> professorsVo  = new HashMap<Long, ProfessorVoVwFull>(18000);
	private static HashMap<Long, SubjectVoVwSmall> subjectsVo = new HashMap<Long, SubjectVoVwSmall>(18000);
	
	public CourseVoVwBuilder(FacadeFactory facadeFactory) {
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
		ProfessorsFacade professorsFacade = facadeFactory.getProfessorsFacade();
		CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
		SubjectVoVwSmall subject = new SubjectVoVwSmall(course.getSubjectVo().getId(),
				course.getSubjectVo().getName(),
				course.getSubjectVo().getCode(), subjectFacade.getSubjectsRatingSummary(course.getSubjectVo().getId()));
		ProfessorVoVwFull professor = new ProfessorVoVwFull(course.getProfessorVo(),
				professorsFacade.getProfessorRatingSummary(course.getProfessorVo().getId()));
		return new CourseVoVwFull(course.getVo(), subject, professor);
	}
	
	public List<CourseVoVwFull> getSearchResults(String query) {
		CoursesFacade coursesFacade = facadeFactory.getCoursesFacade();
		
		Date beforeSearch = new Date();
		List<Long> search = coursesFacade.search(query);
		Date afterSearch = new Date();
		Config.getInstance().getServiceFactory().getLogService().info("Searching for "+ query +" took : " + String.valueOf(afterSearch.getTime()-beforeSearch.getTime()));
		Date beforeConversion = new Date();
		ArrayList<CourseVoVwFull> results = new ArrayList<CourseVoVwFull>(search.size());
		
		ProfessorVoVwFull professor;
		SubjectVoVwSmall subject;
		for (Long courseId : search) {
			CourseVoFull course = coursesFacade.getCourse(courseId);
			Long professorId = course.getProfessorVo().getId();
			Long subjectId = course.getSubjectVo().getId();
			//Loof for the VoVw instances of the professor of this course 
			if (professorsVo.containsKey(professorId)) {
				professor = professorsVo.get(professorId);
			} else {
				//The instance of this professor doesn't exists, create it
				professor = new ProfessorVoVwFull(course.getProfessorVo(), null);
				professorsVo.put(professorId, professor);
			}
			//Loof for the VoVw instances of the subject of this course 
			if (subjectsVo.containsKey(subjectId)) {
				subject = subjectsVo.get(subjectId);
			} else {
				//The instance of this subject doesn't exists, create it
				subject = new SubjectVoVwSmall(subjectId, course.getSubjectVo().getName(), course.getSubjectVo().getCode(), null);
				subjectsVo.put(subjectId, subject);
			}
			//Now that you have the correct instances create the specific course VoVw
			results.add(new CourseVoVwFull(course.getVo(), subject, professor));
		}
		Date afterConversion = new Date();
		Config.getInstance().getServiceFactory().getLogService().info("Converting search results for "+ query +" took : " + String.valueOf(afterConversion.getTime()-beforeConversion.getTime()));

		return results;
		
		
	}
}
