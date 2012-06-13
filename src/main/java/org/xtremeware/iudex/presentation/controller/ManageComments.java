/*
 */
package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.presentation.helper.ViewHelper;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwMedium;
import org.xtremeware.iudex.vo.CommentVo;

/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class ManageComments implements Serializable {

	private String content;
	private boolean anonymous;
	@ManagedProperty(value = "#{viewCourse}")
	private ViewCourse viewCourse;
	@ManagedProperty(value = "#{user}")
	private User user;

	/**
	 * Creates a new instance of ManageComments
	 */
	public ManageComments() {
	}

	private CommentVo createCommentVo() {

		CommentVo comment = new CommentVo();
		comment.setAnonymous(anonymous);
		comment.setContent(content);
		Date now = new Date();
		comment.setDate(now);
		comment.setCourseId(viewCourse.getCourse().getId());
		comment.setRating((float) viewCourse.getCourseRating());
		comment.setUserId(user.getId());
		return comment;
	}

	public void createComment() {
		if (user != null && user.isLoggedIn()) {
			try {
				CommentVo comment = createCommentVo();
				CommentVo createdComment = Config.getInstance().getFacadeFactory().getCommentsFacade().createComment(comment);
				viewCourse.setReRenderComments(true);
				viewCourse.setLatestUserComment(createdComment.getId());
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu comentario ha sido recibido", "Has comentado acerca de este profesor exitosamente. !Agradecemos tu opinión!");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (MultipleMessagesException ex) {
				StringBuffer fullMessage = new StringBuffer();
				for (String message : ex.getMessages()) {
					fullMessage.append(Config.getInstance().getFacadeFactory().getExceptionsFacade().getMessage(message, FacesContext.getCurrentInstance().getViewRoot().
							getLocale()));
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear tu comentario.", fullMessage.toString()));
			} catch (MaxCommentsLimitReachedException ex) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear tu comentario.", "Has alcanzado tu máximo de comentarios por el día de hoy. Disculpanos por los inconvenientes."));
			} catch (DuplicityException ex) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear tu comentario.", "Esto fue debido a un error interno, por favor discúlpanos, pronto nuestros administradores se encargarán del problema."));
				Config.getInstance().getServiceFactory().getLogService().error(ex);
			}
		}
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ViewCourse getViewCourse() {
		return viewCourse;
	}

	public void setViewCourse(ViewCourse viewCourse) {
		this.viewCourse = viewCourse;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void deleteComment(CommentVoVwMedium comment){
		if (user != null && user.isLoggedIn()) {
			if (user.getId().equals(comment.getUser().getId())){
				try {
					Config.getInstance().getFacadeFactory().getCommentsFacade().deleteComment(comment.getId());
					Config.getInstance().getServiceFactory().getLogService().info("Deleting comment with id " + comment.getId());
					viewCourse.setReRenderComments(true);
				} catch (DataBaseException ex) {
					Config.getInstance().getServiceFactory().getLogService().error(ex);
				}
			}else{

					Config.getInstance().getServiceFactory().getLogService().info("NOT Deleting comment with id " + comment.getId() + "because of mismatch between comment user id (" + comment.getUser().getId() + ") and actual user id (" + user.getId() +") .");
			}
		}
	}
}
