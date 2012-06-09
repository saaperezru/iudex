package org.xtremeware.iudex.presentation.vovw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego Gerena (SNIPERCAT) <dagerenaq@gmail.com>
 */
public class CourseListVoVwSmall implements Serializable {
    
    Long ProfessorId;
    String ProfessorName;
    Long SubjectId;
    String SubjectName;
    List<CourseVoVwSmall> CoursesVoVwSmall = new ArrayList<CourseVoVwSmall>();

    public CourseListVoVwSmall(Long ProfessorId, String ProfessorName, Long SubjectId, String SubjectName) {
        this.ProfessorId = ProfessorId;
        this.ProfessorName = ProfessorName;
        this.SubjectId = SubjectId;
        this.SubjectName = SubjectName;
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
        if (this.ProfessorId != other.ProfessorId && (this.ProfessorId == null || !this.ProfessorId.equals(other.ProfessorId))) {
            return false;
        }
        if (this.SubjectId != other.SubjectId && (this.SubjectId == null || !this.SubjectId.equals(other.SubjectId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.ProfessorId != null ? this.ProfessorId.hashCode() : 0);
        hash = 83 * hash + (this.SubjectId != null ? this.SubjectId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseListVoVwSmall{" + "ProfessorId=" + ProfessorId + ", ProfessorName=" + ProfessorName + ", SubjectId=" + SubjectId + ", SubjectName=" + SubjectName + ", CoursesVoVwSmall=" + CoursesVoVwSmall + '}';
    }

    public Long getProfessorId() {
        return ProfessorId;
    }

    public void setProfessorId(Long ProfessorId) {
        this.ProfessorId = ProfessorId;
    }

    public String getProfessorName() {
        return ProfessorName;
    }

    public void setProfessorName(String ProfessorName) {
        this.ProfessorName = ProfessorName;
    }

    public Long getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(Long SubjectId) {
        this.SubjectId = SubjectId;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String SubjectName) {
        this.SubjectName = SubjectName;
    }

    public List<CourseVoVwSmall> getCoursesVoVwSmall(){
        return CoursesVoVwSmall;
    }
    
    public void setCoursesVoVwSmall(List<CourseVoVwSmall> CoursesVoVwSmall) {
        this.CoursesVoVwSmall = CoursesVoVwSmall;
    }
    
    public void addCourse(CourseVoVwSmall courseVovWSmall){
        this.CoursesVoVwSmall.add(courseVovWSmall);
    }

}
