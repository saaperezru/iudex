package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.*;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.facade.SubjectsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ExternalServiceException;
import org.xtremeware.iudex.presentation.vovw.*;
import org.xtremeware.iudex.vo.*;

public final class CourseVoVwBuilder {

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

    public CourseVoVwLarge getCourseVoVwFull(long courseId) {
        SubjectsFacade subjectFacade = facadeFactory.getSubjectsFacade();
        CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
        SubjectVoVwSmall subject = new SubjectVoVwSmall(course.getSubjectVo().getId(),
                course.getSubjectVo().getName(),
                course.getSubjectVo().getCode(), subjectFacade.getSubjectsRatingSummary(course.getSubjectVo().getId()));
        ProfessorVoVwLarge professor = ProfessorVoVwBuilder.getInstance().getProfessorFull(course.getProfessorVo());
        PeriodVo period = facadeFactory.getPeriodsFacade().getPeriod(course.getVo().getPeriodId());
        String periodString;
        if (period == null) {
            periodString = NO_PERIOD_STRING;
        } else {
            periodString = period.getYear() + " - " + period.getSemester();
        }
        return new CourseVoVwLarge(course.getVo(), subject, professor, periodString);
    }

    public CourseVoVwSmall getCourseVoVwSmall(Long courseId) {

        CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
        PeriodVo periodVo = facadeFactory.getPeriodsFacade().getPeriod(course.getVo().getPeriodId());
        CourseVoVwSmall courseSmall = new CourseVoVwSmall(courseId,
                periodVo.getYear() + " - " + periodVo.getSemester(),
                course.getVo().getRatingAverage(), course.getVo().getRatingCount());
        return courseSmall;
    }

    public List<CourseListVoVwSmall> getSearchResults(String query, int itemsByPage, int totalHints) {

        Date beforeSearch = new Date();
        List<Long> search = getSearchCourses(query, totalHints);
        if (totalHints <= search.size()) {
            search = search.subList(totalHints - itemsByPage, totalHints);
        } else {
            search = search.subList(totalHints - itemsByPage, search.size());
        }
        Date afterSearch = new Date();
        Config.getInstance().getServiceFactory().getLogService().info("Searching for " + query + " took : " + String.valueOf(afterSearch.getTime() - beforeSearch.getTime()));

        ArrayList<CourseListVoVwSmall> results = new ArrayList<CourseListVoVwSmall>();

        HashMap<Long, String> professorsName = new HashMap<Long, String>();
        HashMap<Long, String> subjectsName = new HashMap<Long, String>();
        HashMap<Integer, CourseListVoVwSmall> coursesList = new HashMap<Integer, CourseListVoVwSmall>();

        SubjectVoVwSmall subject;

        for (Long courseId : search) {
            CourseVoFull course = facadeFactory.getCoursesFacade().getCourse(courseId);
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
                subjectsName.put(subjectId, "(" + subject.getCode() + ") " + subject.getName());
            }

            //Look for the instance of the CourseListVovw, but first create one to get the hash key
            CourseListVoVwSmall courseList = new CourseListVoVwSmall(professorId, professorsName.get(professorId), subjectId, subjectsName.get(subjectId));
            if (!coursesList.containsKey(courseList.hashCode())) {
                //The instance of this subject doesn't exists in the hash, add it
                coursesList.put(courseList.hashCode(), courseList);
                results.add(courseList);
            }
            //Now that the Course exist in the hastTable add the course ID
            coursesList.get(courseList.hashCode()).addCourse(getCourseVoVwSmall(courseId));
        }
        return results;
    }

    private List getSearchCourses(String query, int totalHints) {
        List<Long> search = null;
        try {
            search = facadeFactory.getCoursesFacade().search(query, totalHints);
        } catch (ExternalServiceException exception) {
            search = new ArrayList<Long>();
        }
        return search;
    }

    public int getSearchCount(String query, int maxitems) {
        return getSearchCourses(query, maxitems).size();
    }
}
