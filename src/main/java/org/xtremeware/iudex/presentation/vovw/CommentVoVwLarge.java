package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

public class CommentVoVwLarge extends CommentVoVwMedium {

    private CourseVoVwLarge course;

    public CommentVoVwLarge() {
    }

    public CommentVoVwLarge(CommentVo vo, UserVoVwSmall user,
            RatingSummaryVo rating) {
        super(vo, user, rating);
    }

    @Override
    public String toString() {
        return "CommentVoVwMedium{" + "vo=" + vo + ", user=" + user +
                ", rating=" +
                rating + ", course=" + course + '}';
    }

    public CourseVoVwLarge getCourse() {
        return course;
    }

    public void setCourse(CourseVoVwLarge course) {
        this.course = course;
    }

    public String getSubject() {
        return course.getSubject().getName();
    }

    public String getProfessor() {
        ProfessorVoVwLarge professor = course.getProfessor();
        return professor.getFirstName() + " " + professor.getLastName();
    }

    public String getPeriod() {
        return course.getPeriod();
    }
}
