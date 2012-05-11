package org.xtremeware.iudex.presentation.controller;

import java.util.List;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwFull;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class ViewCourse {

	private CourseVoVwFull course;
	private List<CommentVoVwFull> comments;
	private ProfessorVoVwFull professor;
	private SubjectVoVwFull subject;
	@ManagedProperty(value = "#{param['id']}")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<CommentVoVwFull> getComments() {
		return comments;
	}

	public void setComments(List<CommentVoVwFull> comments) {
		this.comments = comments;
	}

	public CourseVoVwFull getCourse() {
		return course;
	}

	public void setCourse(CourseVoVwFull course) {
		this.course = course;
	}

	public ProfessorVoVwFull getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorVoVwFull professor) {
		this.professor = professor;
	}

	public SubjectVoVwFull getSubject() {
		return subject;
	}

	public void setSubject(SubjectVoVwFull subject) {
		this.subject = subject;
	}

	public void preRenderView() {
		try {
			course = Config.getInstance().getFacadeFactory().getCoursesFacade().getCourse(id);
			if (course == null){
				((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
			}
			comments = Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentsByCourseId(id);
			professor = Config.getInstance().getFacadeFactory().getProfessorsFacade().getProfessor(course.getSubject().getId());
			subject = Config.getInstance().getFacadeFactory().getSubjectsFacade().getSubject(course.getProfessor().getId());

		} catch (Exception ex) {
			Config.getInstance().getServiceFactory().createLogService().error(ex.getMessage(), ex);
			((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
		}
	}
}
