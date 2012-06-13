package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.facade.CommentsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwMedium;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwLarge;
import org.xtremeware.iudex.vo.BinaryRatingVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Rating implements Serializable {

	private static enum ratingState {

		NO_CHANGE, FROM_NEUTRAL, FROM_OPPOSED, TO_NEUTRAL
	};
	private RatingInterface commentRatingInterface = new RatingInterface() {

		@Override
		public BinaryRatingVo getByObjectAndUserId(long objectId, long userId) {
			return Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentRatingByUserId(objectId, userId);
		}

		@Override
		public void rate(long objectId, long userId, int finalValue) throws MultipleMessagesException, DuplicityException {
			Config.getInstance().getFacadeFactory().getCommentsFacade().rateComment(objectId, userId,finalValue);
		}

	};
	private RatingInterface professorRatingInterface = new RatingInterface() {

		@Override
		public BinaryRatingVo getByObjectAndUserId(long objectId, long userId) {
			return Config.getInstance().getFacadeFactory().getProfessorsFacade().getProfessorRatingByUserId(objectId, userId);
		}

		@Override
		public void rate(long objectId, long userId, int finalValue) throws MultipleMessagesException, DuplicityException {
			Config.getInstance().getFacadeFactory().getProfessorsFacade().rateProfessor(objectId, userId,finalValue);
		}

	};
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
	 * @return NO_CHANGE if no change was done. FROM_NEUTRAL if the vote was
	 * neutral and changed. FROM_OPPOSED if the vote was opposed and changed.
	 * TO_NEUTRAL if the vote was already the desired rateValue.
	 * @throws DataBaseException
	 */
	private ratingState rate(long objectId, int value, RatingInterface ratingInterface) {
		ratingState returnValue = ratingState.NO_CHANGE;
		if (user != null && user.isLoggedIn()) {
			Long userId = user.getId();
			int finalValue = 0; // Value to be stored in the comment
			try {
				BinaryRatingVo ratingByUserId = ratingInterface.getByObjectAndUserId(objectId, userId);
				if (ratingByUserId != null) {
					int actualValue = ratingByUserId.getValue();
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
				} else {
					finalValue = value;
					returnValue = ratingState.FROM_NEUTRAL;
				}
				ratingInterface.rate(objectId, userId, finalValue);
			} catch (Exception ex) {
				Config.getInstance().getServiceFactory().getLogService().error(ex);
				returnValue = ratingState.NO_CHANGE;
			}
		}
		return returnValue;

	}

	public void voteNegativeComment(CommentVoVwMedium comment) {
		switch (rate(comment.getId(), -1,commentRatingInterface)) {
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

	public void votePositiveComment(CommentVoVwMedium comment) throws DataBaseException {
		switch (rate(comment.getId(), 1,commentRatingInterface)) {
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

	public void voteNegativeProfessor(ProfessorVoVwLarge professor) {
		switch (rate(professor.getId(), -1,professorRatingInterface)) {
			case FROM_OPPOSED:
				professor.getRatingSummary().setPositive(professor.getRatingSummary().getPositive() - 1);
			case FROM_NEUTRAL:
				professor.getRatingSummary().setNegative(professor.getRatingSummary().getNegative() + 1);
				break;
			case TO_NEUTRAL:
				professor.getRatingSummary().setNegative(professor.getRatingSummary().getNegative() - 1);
				break;
		}
	}

	public void votePositiveProfessor(ProfessorVoVwLarge professor) throws DataBaseException {
		switch (rate(professor.getId(), 1,professorRatingInterface)) {
			case FROM_OPPOSED:
				professor.getRatingSummary().setNegative(professor.getRatingSummary().getNegative() - 1);
			case FROM_NEUTRAL:
				professor.getRatingSummary().setPositive(professor.getRatingSummary().getPositive() + 1);
				break;
			case TO_NEUTRAL:
				professor.getRatingSummary().setPositive(professor.getRatingSummary().getPositive() - 1);
				break;
		}
	}

	private interface RatingInterface {

		BinaryRatingVo getByObjectAndUserId(long objectId, long userId);

		void rate(long objectId, long userId, int finalValue) throws MultipleMessagesException, DuplicityException ;
	}
}
