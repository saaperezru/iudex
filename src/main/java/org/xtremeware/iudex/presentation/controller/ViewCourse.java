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
import org.xtremeware.iudex.presentation.vovw.CommentVoVwMedium;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.builder.CommentVoVwBuilder;
import org.xtremeware.iudex.presentation.vovw.builder.CourseVoVwBuilder;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class ViewCourse implements Serializable {


	public ViewCourse() {
		id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
	}
	private CourseVoVwLarge course;
	private List<CommentVoVwMedium> comments;
	private Long id;
	private CourseRatingVo courseRating;
	@ManagedProperty(value = "#{user}")
	private User user;

	public void setId(Long id) {
		this.id = id;
	}

	public List<CommentVoVwMedium> getComments() {
		return comments;
	}

	public void setComments(List<CommentVoVwMedium> comments) {
		this.comments = comments;
	}

	public CourseVoVwLarge getCourse() {
		return course;
	}

	public void setCourse(CourseVoVwLarge course) {
		this.course = course;
	}

	public ProfessorVoVwLarge getProfessor() {
		return course.getProfessor();
	}

	public SubjectVoVwSmall getSubject() {
		return course.getSubject();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int parseUserCommentVote(long commentId) {
		int userCommentVoteValue = 0;
		if (user != null && user.isLoggedIn()) {
			BinaryRatingVo commentRatingByUserId = Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentRatingByUserId(commentId, user.getId());
			if (commentRatingByUserId!=null) userCommentVoteValue = commentRatingByUserId.getValue();
		}
		return userCommentVoteValue;
	}

	public double getCourseRating() {
		if (user != null && user.isLoggedIn()) {
			courseRating = Config.getInstance().getFacadeFactory().getCoursesFacade().getCourseRatingByUserId(id, user.getId());
			if (courseRating == null) {
				courseRating = new CourseRatingVo();
				courseRating.setCourseId(id);
				courseRating.setUserId(user.getId());
			}
		}
		return courseRating.getValue();
	}

	public void setCourseRating(double rating) {
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


	public void handleRate(RateEvent rateEvent) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación exitosa", "Has calificado esta materia con un : " + ((Double) rateEvent.getRating()).intValue() + ". ¡Agradecemos tu opinión!");

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void preRenderView() {
		if (id != null) {
			FacadeFactory facadesFactory = Config.getInstance().getFacadeFactory();
			try {
				course = CourseVoVwBuilder.getInstance().getCourseVoVwFull(id);
				if (course == null) {
					((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
				}
				comments = CommentVoVwBuilder.getInstance().getCommentsByCourseId(id);
			} catch (Exception ex) {
				Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
				((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
			}
		}
	}
}
