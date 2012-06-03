package org.xtremeware.iudex.vo;

public class CourseRatingVo extends IdentifiableValueObject<Long> implements ValueObject {

    private Long courseId;
    private Long userId;
    private float value;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseRatingVo other = (CourseRatingVo) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.courseId != other.courseId && (this.courseId == null || !this.courseId.equals(other.courseId))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 59 * hash + (this.courseId != null ? this.courseId.hashCode() : 0);
        hash = 59 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 59 * hash + Float.floatToIntBits(this.value);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseRatingVo{" + "id=" + getId() + ", courseId=" + courseId + ", userId=" + userId + ", value=" + value + '}';
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
