
package org.xtremeware.iudex.vo;

import java.util.Date;

public class CourseVo extends ValueObject{

	private Long id;
	private ProfessorVo professor;
	private SubjectVo subject;
	private PeriodVo period;
	private Double ratingAverage;
	private Long ratingCount;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PeriodVo getPeriod() {
		return period;
	}

	public void setPeriod(PeriodVo period) {
		this.period = period;
	}

	public ProfessorVo getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorVo professor) {
		this.professor = professor;
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

	public SubjectVo getSubject() {
		return subject;
	}

	public void setSubject(SubjectVo subject) {
		this.subject = subject;
	}

}
