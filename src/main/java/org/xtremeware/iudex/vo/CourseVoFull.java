package org.xtremeware.iudex.vo;

public class CourseVoFull implements ValueObject {

    private CourseVo vo;
    private SubjectVo subjectVo;
    private ProfessorVo professorVo;

    public CourseVoFull(CourseVo vo, SubjectVo subjectVo, ProfessorVo professorVo) {
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
        final CourseVoFull other = (CourseVoFull) obj;
        if (this.vo != other.vo && (this.vo == null || !this.vo.equals(other.vo))) {
            return false;
        }
        if (this.subjectVo != other.subjectVo && (this.subjectVo == null || !this.subjectVo.equals(other.subjectVo))) {
            return false;
        }
        if (this.professorVo != other.professorVo && (this.professorVo == null || !this.professorVo.equals(other.professorVo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 19 * hash + (this.subjectVo != null ? this.subjectVo.hashCode() : 0);
        hash = 19 * hash + (this.professorVo != null ? this.professorVo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseVoFull{" + "vo=" + vo + ", subjectVo=" + subjectVo + ", professorVo=" + professorVo + '}';
    }

    public ProfessorVo getProfessorVo() {
        return professorVo;
    }

    public void setProfessorVo(ProfessorVo professorVo) {
        this.professorVo = professorVo;
    }

    public SubjectVo getSubjectVo() {
        return subjectVo;
    }

    public void setSubjectVo(SubjectVo subjectVo) {
        this.subjectVo = subjectVo;
    }

    public CourseVo getVo() {
        return vo;
    }

    public void setVo(CourseVo vo) {
        this.vo = vo;
    }
}
