package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.*;

/**
 *
 * @author josebermeo
 */
public class VoVwFactory {

    public static UserVoVwSmall getUserVoVwSmall(long courseId, UserVo userVo) {
        return new UserVoVwSmall(courseId,
                userVo.getFirstName() + " " + userVo.getLastName(),
                userVo.getUserName());
    }

    public static CommentVoVwFull getCommentVoVwFull(CommentVo commentVo,
            UserVoVwSmall userVoVwSmall, RatingSummaryVo ratingSummaryVo) {
        return new CommentVoVwFull(commentVo, userVoVwSmall, ratingSummaryVo);
    }

    public static ProfessorVoVwSmall getProfessorVoVwSmall(ProfessorVo professorVo, RatingSummaryVo rating) {
        return new ProfessorVoVwSmall(professorVo.getId(),
                professorVo.getFirstName() + " " + professorVo.getLastName(), rating);
    }

    public static SubjectVoVwSmall getSubjectVoVwSmall(SubjectVo subjectVo, RatingSummaryVo rating) {
        return new SubjectVoVwSmall(subjectVo.getId(), subjectVo.getName(), subjectVo.getCode(), rating);
    }

    public static CourseVoVwFull getCourseVoVwFull(CourseVo courseVo, SubjectVoVwSmall subjectVoVwSmall, ProfessorVoVwSmall professorVoVwSmall) {
        return new CourseVoVwFull(courseVo, subjectVoVwSmall, professorVoVwSmall);
    }

    public static ProfessorVoVwFull getProfessorVoVwFull(ProfessorVo professorVo, RatingSummaryVo ratingSummaryVo) {
        return new ProfessorVoVwFull(professorVo, ratingSummaryVo);
    }

    public static SubjectVoVwFull getSubjectVoVwFull(SubjectVo subjectVo, RatingSummaryVo ratingSummaryVo) {
        return new SubjectVoVwFull(subjectVo, ratingSummaryVo);
    }
}
