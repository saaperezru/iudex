
package org.xtremeware.iudex.vo;

public class CourseVoFull implements ValueObject{

	private CourseVo vo;
	private SubjectVoSmall subjectVoSmall;
	private ProfessorVoSmall professorVoSmall;


	public CourseVoFull(CourseVo vo, SubjectVoSmall subjectVo, ProfessorVoSmall professorVo) {
		this.vo = vo;
		this.subjectVoSmall = subjectVo;
		this.professorVoSmall = professorVo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CourseVoFull other = (CourseVoFull) obj;
		if ((this.vo == null || !this.vo.equals(other.vo))) {
			return false;
		}
		if ((this.subjectVoSmall == null || !this.subjectVoSmall.equals(other.subjectVoSmall))) {
			return false;
		}
		if ((this.professorVoSmall == null || !this.professorVoSmall.equals(other.professorVoSmall))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + (this.vo != null ? this.vo.hashCode() : 0);
		hash = 23 * hash + (this.subjectVoSmall != null ? this.subjectVoSmall.hashCode() : 0);
		hash = 23 * hash + (this.professorVoSmall != null ? this.professorVoSmall.hashCode() : 0);
		return hash;
	}



	@Override
	public String toString() {
		return "CourseVoVwFull{" + vo.toString() + "subjectVo=" + subjectVoSmall.toString() + ", professorVo=" + professorVoSmall.toString() + '}';
	}

	public Long getId() {
		return vo.getId();
	}

	public ProfessorVoSmall getProfessor() {
		return professorVoSmall;
	}

	public SubjectVoSmall getSubject() {
		return subjectVoSmall;
	}

	public Double getRatingAverage() {
		return vo.getRatingAverage();
	}

	public Long getRatingCount() {
		return vo.getRatingCount();
	}


}
