package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.ValueObject;

public class CourseVoVwLarge implements ValueObject {

    private CourseVo vo;
    private SubjectVoVwSmall subjectVo;
    private ProfessorVoVwLarge professorVo;
    private String period;

    public CourseVoVwLarge(CourseVo vo, SubjectVoVwSmall subjectVo,
            ProfessorVoVwLarge professorVo, String period) {
        this.vo = vo;
        this.subjectVo = subjectVo;
        this.professorVo = professorVo;
        this.period = period;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseVoVwLarge other = (CourseVoVwLarge) obj;
        if ((this.vo == null || !this.vo.equals(other.vo))) {
            return false;
        }
        if ((this.subjectVo == null || !this.subjectVo.equals(other.subjectVo))) {
            return false;
        }
        if ((this.professorVo == null || !this.professorVo.equals(
                other.professorVo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 23 * hash + (this.subjectVo != null ? this.subjectVo.hashCode() :
                0);
        hash = 23 * hash + (this.professorVo != null ?
                this.professorVo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseVoVwLarge{" + "vo=" + vo + ", subjectVo=" + subjectVo +
                ", professorVo=" + professorVo + ", period=" + period + '}';
    }

    public Long getId() {
        return vo.getId();
    }

    public ProfessorVoVwLarge getProfessor() {
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
