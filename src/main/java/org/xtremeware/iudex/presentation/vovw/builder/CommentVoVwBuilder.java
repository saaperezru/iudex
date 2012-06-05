package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xtremeware.iudex.businesslogic.facade.CommentsFacade;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.UserVo;

public class CommentVoVwBuilder {

	private static CommentVoVwBuilder instance;
	private FacadeFactory facadeFactory;

	private CommentVoVwBuilder(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	public static synchronized CommentVoVwBuilder getInstance() {
		if (instance == null) {
			instance = new CommentVoVwBuilder(Config.getInstance().getFacadeFactory());
		}
		return instance;
	}

	public List<CommentVoVwFull> getCommentsByCourseId(long courseId) {
		List<CommentVo> commentsVo = facadeFactory.getCommentsFacade().getCommentsByCourseId(courseId);
		ArrayList<CommentVoVwFull> commentsList = new ArrayList<CommentVoVwFull>(commentsVo.size());
		HashMap<Long, UserVoVwSmall> users = new HashMap<Long, UserVoVwSmall>();
		UserVoVwSmall anonymousUser = UserVoVwBuilder.anonymousUser;
		CommentsFacade commentsFacade = facadeFactory.getCommentsFacade();

		UserVoVwSmall commentUser;
		for (CommentVo commentVo : commentsVo) {
			if (commentVo.isAnonymous()) {
				commentUser = anonymousUser;
			} else {
				long userId = commentVo.getUserId();
				if (!users.containsKey(userId)) {
					users.put(userId, UserVoVwBuilder.getInstance().getUser(userId));
				}
				commentUser = users.get(userId);
			}
			commentsList.add(new CommentVoVwFull(commentVo, commentUser, commentsFacade.getCommentRatingSummary(commentVo.getId())));
		}
		return commentsList;
	}
}
