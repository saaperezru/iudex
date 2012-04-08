package org.xtremeware.iudex.presentation.vovw;

import java.util.Date;
import org.xtremeware.iudex.vo.CommentVo;

public class CommentVoVwFull {

	private CommentVo vo;
	private UserVoVwSmall user;

	public CommentVoVwFull(CommentVo vo, UserVoVwSmall user) {
		this.vo = vo;
		if (vo.isAnonymous()) {
			this.user = null;
		} else {
			this.user = user;
		}
	}

	@Override
	public String toString() {
		return "CommentVoVwFull{" + "vo=" + vo.toString() + ", user=" + user.toString() + '}';
	}

	public boolean isAnonymous() {
		return vo.isAnonymous();
	}

	public String getContent() {
		return vo.getContent();
	}

	public Date getDate() {
		return vo.getDate();
	}

	public Long getId() {
		return vo.getId();
	}

	public Float getRating() {
		return vo.getRating();
	}

	public UserVoVwSmall getUser() {
		return user;
	}
}
