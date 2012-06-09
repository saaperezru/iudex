package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.xtremeware.iudex.businesslogic.facade.CoursesFacade;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.facade.SubjectsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.*;
import org.xtremeware.iudex.vo.CourseVoFull;
import org.xtremeware.iudex.vo.PeriodVo;
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
        
        public  CourseVoVwSmall getCourseVoVwSmall(Long courseId){
                // TODO: use Get period by ID
            
                //Get all periods and add them to a hasTable to find them faster
                List<PeriodVo> periodVoList = facadeFactory.getPeriodsFacade().listPeriods();
                HashMap<Long, PeriodVo> periodHash = new HashMap<Long, PeriodVo>();
                for (PeriodVo periodVo : periodVoList){
                    if(! periodHash.containsKey(periodVo.getId())){
                        periodHash.put(periodVo.getId(), periodVo);
                    }
                }                
                // For each course ID find it's Vo, create the VovWSmall and add it to the list.
                CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
                CourseVoVwSmall courseSmall = new CourseVoVwSmall(courseId, 
                                                                    periodHash.get(course.getVo().getPeriodId()).getYear()+" - "+periodHash.get(course.getVo().getPeriodId()).getSemester(),
                                                                    course.getVo().getRatingAverage(), course.getVo().getRatingCount());                   
                return courseSmall;
        }

	public List<CourseListVoVwSmall> getSearchResults(String query) {
		CoursesFacade coursesFacade = facadeFactory.getCoursesFacade();

		Date beforeSearch = new Date();
		List<Long> search = coursesFacade.search(query);
		Date afterSearch = new Date();
		Config.getInstance().getServiceFactory().getLogService().info("Searching for " + query + " took : " + String.valueOf(afterSearch.getTime() - beforeSearch.getTime()));
		
                ArrayList<CourseListVoVwSmall> results = new ArrayList<CourseListVoVwSmall>();
                
		HashMap<Long, String> professorsName = new HashMap<Long, String>();
		HashMap<Long, String> subjectsName = new HashMap<Long, String>();
                HashMap<Integer,CourseListVoVwSmall> coursesList = new HashMap<Integer,CourseListVoVwSmall>();

		SubjectVoVwSmall subject;
                
		for (Long courseId : search) {
                    CourseVoFull course = coursesFacade.getCourse(courseId);
                    Long professorId = course.getProfessorVo().getId();
                    Long subjectId = course.getSubjectVo().getId();
                    //Look for the Name of the professor of this course 
                    if (!professorsName.containsKey(professorId)) {
                            //The Name of this professor doesn't exists, create it
                            professorsName.put(professorId, ProfessorVoVwBuilder.getInstance().getProfessorFull(professorId).getFullName());
                    }
                    //Look for the Name instances of the subject of this course 
                    if (!subjectsName.containsKey(subjectId)) {
                        //The instance of this subject doesn't exists, create it
                            SubjectVoFull subjectFull = facadeFactory.getSubjectsFacade().getSubject(subjectId);
                            subject = new SubjectVoVwSmall(subjectId, subjectFull.getVo().getName(), subjectFull.getVo().getCode(), subjectFull.getRatingSummary());
                            subjectsName.put(subjectId, "("+subject.getCode()+") "+subject.getName());
                    }

                    //Look for the instance of the CourseListVovw, but first create one to get the hash key
                    CourseListVoVwSmall courseList = new CourseListVoVwSmall(professorId, professorsName.get(professorId), subjectId, subjectsName.get(subjectId));
                    if(!coursesList.containsKey(courseList.hashCode())){
                        //The instance of this subject doesn't exists in the hash, add it
                        coursesList.put(courseList.hashCode(), courseList);
                        results.add(courseList);
                    }
                    //Now that the Course exist in the hastTable add the course ID
                    coursesList.get(courseList.hashCode()).addCourse(getCourseVoVwSmall(courseId));	
		}

		return results;


	}
}
