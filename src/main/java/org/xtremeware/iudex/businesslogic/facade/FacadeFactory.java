
package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public class FacadeFactory {
	private CoursesFacade coursesFacade;
	private ProfessorsFacade professorsFacade;
	private SubjectsFacade subjectsFacade;
	private PeriodsFacade periodsFacade;
	private UsersFacade usersFacade;
	private CommentsFacade commentsFacade;
	private FeedbacksFacade feedbacksFacade;
	private ProgramsFacade programsFacade;

	public FacadeFactory(ServiceFactory serviceFactory,EntityManagerFactory emFactory){
		coursesFacade = new CoursesFacade(serviceFactory, emFactory);
		professorsFacade = new ProfessorsFacade(serviceFactory, emFactory);
		subjectsFacade = new SubjectsFacade(serviceFactory, emFactory);
		periodsFacade = new PeriodsFacade(serviceFactory, emFactory);
		usersFacade = new UsersFacade(serviceFactory, emFactory);
		commentsFacade = new CommentsFacade(serviceFactory, emFactory);
		feedbacksFacade = new FeedbacksFacade(serviceFactory, emFactory);
		programsFacade = new ProgramsFacade(serviceFactory, emFactory);
	}

	public CommentsFacade getCommentsFacade() {
		return commentsFacade;
	}

	public CoursesFacade getCoursesFacade() {
		return coursesFacade;
	}

	public FeedbacksFacade getFeedbacksFacade() {
		return feedbacksFacade;
	}

	public PeriodsFacade getPeriodsFacade() {
		return periodsFacade;
	}

	public ProfessorsFacade getProfessorsFacade() {
		return professorsFacade;
	}

	public ProgramsFacade getProgramsFacade() {
		return programsFacade;
	}

	public SubjectsFacade getSubjectsFacade() {
		return subjectsFacade;
	}

	public UsersFacade getUsersFacade() {
		return usersFacade;
	}
}
