
package org.xtremeware.iudex.vo;

import java.util.Date;

public class CommentVo extends ValueObject{

	private Long id;
	private String content;
	private Date date;
	private UserVo user;
	private CourseVo course;
	private boolean anonymous;
	private Float rating;

	@Override
	public boolean equals(Object ob) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int compareTo(ValueObject vo) {
		throw new UnsupportedOperationException("Not supported yet.");
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

	public CourseVo getCourse() {
		return course;
	}

	public void setCourse(CourseVo course) {
		this.course = course;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

}
