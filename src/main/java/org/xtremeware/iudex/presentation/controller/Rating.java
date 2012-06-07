package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.businesslogic.facade.CommentsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.vo.BinaryRatingVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Rating implements Serializable {

	private static enum ratingState{NO_CHANGE,FROM_NEUTRAL,FROM_OPPOSED,TO_NEUTRAL};
    @ManagedProperty(value = "#{user}")
    private User user;

    public void rateComment() {
    }

    public void rateCourse() {
    }

    public void rateSubject() {
    }

    public void rateProfessor() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Communicates with the application facade to actually store the comment
     * rating. The rating action is a toggle action, so this method will check
     * the previous value of the vote. Several things can happen here: - The
     * user wasn't authenticated, so it cannot rate the comment. - The rating
     * changed from positive to negative or viceversa. - The rating changed from
     * neutral to anything. - The rating changed from anything to neutral. Each
     * of this will have a different effect in the RatingSummaryVo. It will be a
     * change in future versions to manage this cases in a proper location.
     *
     * @param comment The comment being rated.
     * @param value The new rating value for the comment.
     * @return 0 if no change was done. 1 if the vote was neutral and changed.
     * -1 if the vote was opposed and changed. 2 if the vote was already the
     * desired rateValue.
     * @throws DataBaseException
     */
    private ratingState rateComment(CommentVoVwFull comment, int value) {
		CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().getCommentsFacade();
        Long commentId = comment.getId();
        ratingState returnValue = ratingState.NO_CHANGE;
        if (user != null && user.isLoggedIn()) {
            Long userId = user.getId();
            int finalValue = 0; // Value to be stored in the comment
            try {
                BinaryRatingVo commentRatingByUserId = commentsFacade.getCommentRatingByUserId(commentId, userId);
                if (commentRatingByUserId != null) {
                    int actualValue = commentRatingByUserId.getValue();
                    if (actualValue == value) {
                        finalValue = 0;
                        returnValue = ratingState.TO_NEUTRAL;
                    } else if (actualValue == 0) {
                        finalValue = value;
                        returnValue = ratingState.FROM_NEUTRAL;
                    } else {
                        finalValue = value;
                        returnValue = ratingState.FROM_OPPOSED;
                    }
                }else{
					finalValue = value;
					returnValue = ratingState.FROM_NEUTRAL;
				}
                Config.getInstance().getFacadeFactory().getCommentsFacade().rateComment(commentId, userId, finalValue);
            } catch (Exception ex) {
                Logger.getLogger(ViewCourse.class.getName()).log(Level.SEVERE, null, ex);
                returnValue = ratingState.NO_CHANGE;
            }
        }
        return returnValue;

    }

    public void voteNegativeComment(CommentVoVwFull comment) {
        switch (rateComment(comment, -1)) {
            case FROM_OPPOSED:
                comment.getRating().setPositive(comment.getRating().getPositive() - 1);
            case FROM_NEUTRAL:
                comment.getRating().setNegative(comment.getRating().getNegative() + 1);
                break;
            case TO_NEUTRAL:
                comment.getRating().setNegative(comment.getRating().getNegative() - 1);
                break;
        }
    }

    public void votePositiveComment(CommentVoVwFull comment) throws DataBaseException {
        switch (rateComment(comment, 1)) {
            case FROM_OPPOSED:
                comment.getRating().setNegative(comment.getRating().getNegative() - 1);
            case FROM_NEUTRAL:
                comment.getRating().setPositive(comment.getRating().getPositive() + 1);
                break;
            case TO_NEUTRAL:
                comment.getRating().setPositive(comment.getRating().getPositive() - 1);
                break;
        }
    }
}
