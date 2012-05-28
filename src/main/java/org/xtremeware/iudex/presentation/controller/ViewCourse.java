/*
 */
package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RateEvent;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.*;
import org.xtremeware.iudex.vo.*;


/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class ViewCourse implements Serializable {
	private static Integer[][] listLists = {{1}, {1, 2}, {1, 2, 3}, {1, 2, 3, 4}, {1, 2, 3, 4, 5}};

	public ViewCourse() {
		id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
	}
	private CourseVoFull course;
	private List<CommentVoFull> comments;
	private ProfessorVoFull professor;
	private SubjectVoFull subject;
	private Long id;
	private CourseRatingVo courseRating;
	@ManagedProperty(value = "#{user}")
	private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public List<CommentVoFull> getComments() {
        return comments;
    }

    public void setComments(List<CommentVoFull> comments) {
        this.comments = comments;
    }

    public CourseVoFull getCourse() {
        return course;
    }

    public void setCourse(CourseVoFull course) {
        this.course = course;
    }

    public ProfessorVoFull getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorVoFull professor) {
        this.professor = professor;
    }

    public SubjectVoFull getSubject() {
        return subject;
    }

    public void setSubject(SubjectVoFull subject) {
        this.subject = subject;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getCourseRating(){
		if (user != null && user.isLoggedIn()) {
			courseRating = Config.getInstance().getFacadeFactory().getCoursesFacade().getCourseRatingByUserId(id, user.getId());
			if (courseRating ==null){
				courseRating = new CourseRatingVo();
				courseRating.setCourseId(id);
				courseRating.setUserId(user.getId());
			} 
		}
		return courseRating.getValue();
	}

	public void setCourseRating(double rating){
		float value = (float) rating;
		this.courseRating.setValue(value);
		try {
			Config.getInstance().getFacadeFactory().getCoursesFacade().rateCourse(id, user.getId(), value);
		} catch (MultipleMessagesException ex) {
			Logger.getLogger(CourseRatingVo.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(CourseRatingVo.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public List<Integer> buildArrayFloor(float size) {
		int index = (int) Math.floor(size);
		if (index < 6 && index > 0) {
			return Arrays.asList(listLists[index - 1]);
		}
		return null;
	}

	public List<Integer> buildArrayCeil(float size) {
		int index = (int) Math.ceil(size);
		if (index < 6 && index > 0) {
			return Arrays.asList(listLists[index - 1]);
		}
		return null;
	}

	public void handleRate(RateEvent rateEvent) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación exitosa", "Has calificado esta materia con un :" + ((Double) rateEvent.getRating()).intValue() + ". ¡Agradecemos tu opinión!");

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void preRenderView() {
		if (id != null) {
			FacadeFactory facadesFactory = Config.getInstance().getFacadeFactory();
			try {
				course = facadesFactory.getCoursesFacade().getCourse(id);
				if (course == null) {
					((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
				}
				professor = facadesFactory.getProfessorsFacade().getProfessor(course.getSubject().getId());
				subject = facadesFactory.getSubjectsFacade().getSubject(course.getProfessor().getId());
				comments = facadesFactory.getCommentsFacade().getCommentsByCourseId(id);
			} catch (Exception ex) {
				Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
				((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
			}
		}
	}
}
