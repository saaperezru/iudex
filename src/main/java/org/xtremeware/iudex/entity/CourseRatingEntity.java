package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.CourseRatingVo;

@javax.persistence.Entity
@NamedQueries({
    @NamedQuery(
        name="getByCourseId",
        query="SELECT result FROM CourseRatingEntity result "+
                "WHERE result.course.id = :CI"
        ),
    @NamedQuery(
        name="getByCourseIdAndUserId", 
        query="SELECT result FROM CourseRatingEntity result "+
                "WHERE result.course.id = :CI AND result.user.id = :UI"
        )
})
public class CourseRatingEntity implements Serializable, Entity<CourseRatingVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private CourseEntity course;
	private UserEntity user;
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
