package org.xtremeware.iudex.vo;

import java.util.Date;


public class CommentVoFull implements ValueObject{

	private CommentVo vo;
	private UserVoSmall user;
	private RatingSummaryVo rating;

	public CommentVoFull(CommentVo vo, UserVoSmall user, RatingSummaryVo rating) {
		this.vo = vo;
		this.rating = rating;		
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
	
	public RatingSummaryVo getRating(){
		return this.rating;
	}

	public Float getCourseRating() {
		return vo.getRating();
	}


	public UserVoSmall getUser() {
		return user;
	}
}
