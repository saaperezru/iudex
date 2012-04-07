package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.CourseRatingVo;

@javax.persistence.Entity(name = "CourseRating")
@NamedQueries({
	@NamedQuery(name = "getCourseRatingByCourseId",
	query = "SELECT result FROM CourseRating result "
	+ "WHERE result.course.id = :courseId"),
	@NamedQuery(name = "getCourseRatingByCourseIdAndUserId",
	query = "SELECT result FROM CourseRating result "
	+ "WHERE result.course.id = :courseId AND result.user.id = :userId")
})
@Table(name="COURSE_RATING")
public class CourseRatingEntity implements Serializable, Entity<CourseRatingVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_COURSE_RATING")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ID_COURSE", nullable = false)
	private CourseEntity course;
	@ManyToOne
	@JoinColumn(name = "ID_USER_", nullable = false)
	private UserEntity user;
	@Column(name = "RATING", nullable = false)
	private float value;

	@Override
	public CourseRatingVo toVo() {
		CourseRatingVo vo = new CourseRatingVo();
		vo.setId(this.getId());
		vo.setCourseId(this.getCourse().getId());
		vo.setUserId(this.getUser().getId());
		vo.setValue(this.getValue());
		return vo;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CourseRatingEntity)) {
			return false;
		}
		CourseRatingEntity other = (CourseRatingEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "CourseRatingEntity{" + "id=" + id + ", course=" + course + ", user=" + user + ", value=" + value + '}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CourseEntity getCourse() {
		return course;
	}

	public void setCourse(CourseEntity course) {
		this.course = course;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
