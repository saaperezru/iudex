package org.xtremeware.iudex.presentation.vovw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego Gerena (SNIPERCAT) <dagerenaq@gmail.com>
 */
public class CourseListVoVwSmall implements Serializable {
    
    private Long professorId;
    private String professorName;
    private Long subjectId;
    private String subjectName;
    private List<CourseVoVwSmall> coursesVoVwSmall = new ArrayList<CourseVoVwSmall>();

    public CourseListVoVwSmall(Long professorId, String professorName, Long subjectId, String subjectName) {
        this.professorId = professorId;
        this.professorName = professorName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseListVoVwSmall other = (CourseListVoVwSmall) obj;
        if (this.professorId != other.professorId && (this.professorId == null || !this.professorId.equals(other.professorId))) {
            return false;
        }
        if (this.subjectId != other.subjectId && (this.subjectId == null || !this.subjectId.equals(other.subjectId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.professorId != null ? this.professorId.hashCode() : 0);
        hash = 83 * hash + (this.subjectId != null ? this.subjectId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseListVoVwSmall{" + "ProfessorId=" + professorId + ", ProfessorName=" + professorName + ", SubjectId=" + subjectId + ", SubjectName=" + subjectName + ", CoursesVoVwSmall=" + coursesVoVwSmall + '}';
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<CourseVoVwSmall> getCoursesVoVwSmall(){
        return coursesVoVwSmall;
    }
    
    public void setCoursesVoVwSmall(List<CourseVoVwSmall> coursesVoVwSmall) {
        this.coursesVoVwSmall = coursesVoVwSmall;
    }
    
    public void addCourse(CourseVoVwSmall courseVovWSmall){
        this.coursesVoVwSmall.add(courseVovWSmall);
    }

}
