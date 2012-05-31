
package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.ValueObject;

public class CourseVoVwFull implements ValueObject{

	private CourseVo vo;
	private SubjectVoVwSmall subjectVo;
	private ProfessorVoVwSmall professorVo;


	public CourseVoVwFull(CourseVo vo, SubjectVoVwSmall subjectVo, ProfessorVoVwSmall professorVo) {
		this.vo = vo;
		this.subjectVo = subjectVo;
		this.professorVo = professorVo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CourseVoVwFull other = (CourseVoVwFull) obj;
		if ((this.vo == null || !this.vo.equals(other.vo))) {
			return false;
		}
		if ((this.subjectVo == null || !this.subjectVo.equals(other.subjectVo))) {
			return false;
		}
		if ((this.professorVo == null || !this.professorVo.equals(other.professorVo))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + (this.vo != null ? this.vo.hashCode() : 0);
		hash = 23 * hash + (this.subjectVo != null ? this.subjectVo.hashCode() : 0);
		hash = 23 * hash + (this.professorVo != null ? this.professorVo.hashCode() : 0);
		return hash;
	}



	@Override
	public String toString() {
		return "CourseVoVwFull{" + vo.toString() + "subjectVo=" + subjectVo.toString() + ", professorVo=" + professorVo.toString() + '}';
	}

	public Long getId() {
		return vo.getId();
	}

	public ProfessorVoVwSmall getProfessor() {
		return professorVo;
	}

	public SubjectVoVwSmall getSubject() {
		return subjectVo;
	}

	public Double getRatingAverage() {
		return vo.getRatingAverage();
	}

	public Long getRatingCount() {
		return vo.getRatingCount();
	}

}
