/*
 */
package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.CommentVo;

/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class CreateComment implements Serializable {

	private String content;
	private boolean anonymous;
	@ManagedProperty(value = "#{viewCourse}")
	private ViewCourse viewCourse;
	@ManagedProperty(value = "#{user}")
	private User user;

	/**
	 * Creates a new instance of CreateComment
	 */
	public CreateComment() {
	}

	public void createComment() {
		if (user != null && user.isLoggedIn()) {
			try {
				CommentVo comment = new CommentVo();
				comment.setAnonymous(anonymous);
				comment.setContent(content);
				Date now = new Date();
				comment.setDate(now);
				comment.setCourseId(viewCourse.getCourse().getId());
				comment.setRating((float) viewCourse.getCourseRating());
				comment.setUserId(user.getId());
				Config.getInstance().getFacadeFactory().getCommentsFacade().createComment(comment);
			} catch (MultipleMessagesException ex) {
				Logger.getLogger(CreateComment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (MaxCommentsLimitReachedException ex) {
				Logger.getLogger(CreateComment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (DuplicityException ex) {
				Logger.getLogger(CreateComment.class.getName()).log(Level.SEVERE, null, ex);
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

}
