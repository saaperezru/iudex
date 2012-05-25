package org.xtremeware.iudex.vo;

public class CourseVo extends IdentifiableValueObject<Long> implements ValueObject {

	private Long professorId;
	private Long subjectId;
	private Long periodId;
	private double ratingAverage;
	private long ratingCount;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CourseVo other = (CourseVo) obj;
		if (this.professorId != other.professorId && (this.professorId == null || !this.professorId.equals(other.professorId))) {
			return false;
		}
		if (this.subjectId != other.subjectId && (this.subjectId == null || !this.subjectId.equals(other.subjectId))) {
			return false;
		}
		if (this.periodId != other.periodId && (this.periodId == null || !this.periodId.equals(other.periodId))) {
			return false;
		}
		if (Double.doubleToLongBits(this.ratingAverage) != Double.doubleToLongBits(other.ratingAverage)) {
			return false;
		}
		if (this.ratingCount != other.ratingCount) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 61 * hash + (this.professorId != null ? this.professorId.hashCode() : 0);
		hash = 61 * hash + (this.subjectId != null ? this.subjectId.hashCode() : 0);
		hash = 61 * hash + (this.periodId != null ? this.periodId.hashCode() : 0);
		hash = 61 * hash + (int) (Double.doubleToLongBits(this.ratingAverage) ^ (Double.doubleToLongBits(this.ratingAverage) >>> 32));
		hash = 61 * hash + (int) (this.ratingCount ^ (this.ratingCount >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "CourseVo{" + "id=" + id + ", professorId="
				+ professorId + ", subjectId=" + subjectId
				+ ", periodId=" + periodId + ", ratingAverage="
				+ ratingAverage + ", ratingCount=" + ratingCount + '}';
	}

	public Long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}

	public Long getProfessorId() {
		return professorId;
	}

	public void setProfessorId(Long professorId) {
		this.professorId = professorId;
	}

	public Double getRatingAverage() {
		return ratingAverage;
	}

	public void setRatingAverage(Double ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
}