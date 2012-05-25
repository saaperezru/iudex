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
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RateEvent;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.*;
import org.xtremeware.iudex.vo.CourseRatingVo;


/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class ViewCourse implements Serializable {
	private Integer[][] listLists = {{1}, {1, 2}, {1, 2, 3}, {1, 2, 3, 4}, {1, 2, 3, 4, 5}};

	public ViewCourse() {
		id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
	}
	private CourseVoVwFull course;
	private List<CommentVoVwFull> comments;
	private ProfessorVoVwFull professor;
	private SubjectVoVwFull subject;
	private Long id;
	private CourseRatingVo courseRating;
	@ManagedProperty(value = "#{user}")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		System.out.println("[DEBUG] Getting the courseRating from View course : " + courseRating.toString());
		return courseRating.getValue();
	}

	public void setCourseRating(double rating){
		float value = (float) rating;
		this.courseRating.setValue(value);
		System.out.println("[DEBUG] Setting the value for course rating : " + courseRating.toString());
		try {
			Config.getInstance().getFacadeFactory().getCoursesFacade().rateCourse(id, user.getId(), value);
		} catch (MultipleMessagesException ex) {
			Logger.getLogger(CourseRatingVo.class.getName()).log(Level.SEVERE, null, ex);
		System.out.println("[DEBUG] FUCKK1!!");
		} catch (Exception ex) {
			Logger.getLogger(CourseRatingVo.class.getName()).log(Level.SEVERE, null, ex);
		System.out.println("[DEBUG] FUCKK2!!");
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
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Double) rateEvent.getRating()).intValue());

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
				Config.getInstance().getServiceFactory().createLogService().error(ex.getMessage(), ex);
				((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
			}
		}
	}
}