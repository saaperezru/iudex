package org.xtremeware.iudex.presentation.vovw;

import java.util.Date;
import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

@ManagedBean
@ViewScoped
public class CommentVoVwFull implements ValueObject{

	private CommentVo vo;
	private UserVoVwSmall user;
	private RatingSummaryVo rating;

	public CommentVoVwFull(CommentVo vo, UserVoVwSmall user, RatingSummaryVo rating) {
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

	public UserVoVwSmall getUser() {
		return user;
	}
}
