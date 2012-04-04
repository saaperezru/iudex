
package org.xtremeware.iudex.vo;

public class CourseRatingVo extends ValueObject{

	private Long id;
	private CourseVo course;
	private UserVo user;
	private float value;

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

	public CourseVo getCourse() {
		return course;
	}

	public void setCourse(CourseVo course) {
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}


}
