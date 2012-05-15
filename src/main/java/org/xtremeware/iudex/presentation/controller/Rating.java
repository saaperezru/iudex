package org.xtremeware.iudex.presentation.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessageException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.vo.CommentRatingVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Rating {

	private static final String USER_SESSION_KEY = "user";

	public void rateComment() {
	}

	public void rateCourse() {
	}

	public void rateSubject() {
	}

	public void rateProfessor() {
	}

	/**
	 * Communicates with the application facade to actually store the
	 * comment rating. It verifies if the user had already voted so that
	 * the rating action is a toggle action.
	 * 
	 *
	 * @param comment
	 * @param value
	 * @return The value actually stored in the database after the rating.
	 * @throws DataBaseException
	 */
	private int voteComment(CommentVoVwFull comment, int value) throws DataBaseException {
		Long commentId = comment.getId();
		int finalValue = -1;
		Long userId = 2L;
//		UserVo user = (UserVo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USER_SESSION_KEY);
//		Long userId = user.getId();
		try {
			//			if (user != null) {
			CommentRatingVo commentRatingByUserId = Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentRatingByUserId(commentId, userId);
			if (commentRatingByUserId != null && commentRatingByUserId.getValue() == value) {
				finalValue = 0;
			} else {
				finalValue = value;
			}
			Config.getInstance().getFacadeFactory().getCommentsFacade().rateComment(commentId, userId, finalValue);
			//			}

		} catch (MultipleMessageException ex) {
			Logger.getLogger(ViewCourse.class.getName()).log(Level.SEVERE, null, ex);
		}
		return finalValue;

	}

	public void voteNegativeComment(CommentVoVwFull comment) {
		try {
			int voteComment = voteComment(comment, -1);
			if (voteComment == 0){
				comment.getRating().setNegative(comment.getRating().getNegative() - 1);	
			}else{
				comment.getRating().setNegative(comment.getRating().getNegative() + 1);	
			}
			
		} catch (DataBaseException e) {
			//PENDING!! Properly handle this exception
			System.out.println("ERROR : A comment could not be negatively voted : " + comment.toString());
		}

	}

	public void votePositiveComment(CommentVoVwFull comment) throws DataBaseException {
		try {
			int voteComment = voteComment(comment, 1);
			if (voteComment == 0){
				comment.getRating().setPositive(comment.getRating().getPositive() - 1);	
			}else{
				comment.getRating().setPositive(comment.getRating().getPositive() + 1);	
			}
			
		} catch (DataBaseException e) {
			//PENDING!! Properly handle this exception
			System.out.println("ERROR : A comment could not be negatively voted : " + comment.toString());
		}
	}
}
