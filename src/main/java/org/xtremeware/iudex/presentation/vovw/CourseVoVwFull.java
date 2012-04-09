
package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.CourseVo;

public class CourseVoVwFull {

	private CourseVo vo;
	private SubjectVoVwSmall subjectVo;
	private ProfessorVoVwSmall professorVo;


	public CourseVoVwFull(CourseVo vo, SubjectVoVwSmall subjectVo, ProfessorVoVwSmall professorVo) {
		this.vo = vo;
		this.subjectVo = subjectVo;
		this.professorVo = professorVo;
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
