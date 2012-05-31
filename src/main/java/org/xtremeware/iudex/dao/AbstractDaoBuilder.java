package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 *
 * @author healarconr
 */
public interface AbstractDaoBuilder {

    CommentDao getCommentDao();

    BinaryRatingDao<CommentRatingEntity> getCommentRatingDao();

    ConfirmationKeyDao getConfirmationKeyDao();

    CourseDao getCourseDao();

    CourseRatingDao getCourseRatingDao();

    FeedbackDao getFeedbackDao();

    FeedbackTypeDao getFeedbackTypeDao();

    ForgottenPasswordKeyDao getForgottenPasswordKeyDao();

    PeriodDao getPeriodDao();

    ProfessorDao getProfessorDao();

    BinaryRatingDao<ProfessorRatingEntity> getProfessorRatingDao();

    ProgramDao getProgramDao();

    SubjectDao getSubjectDao();

    BinaryRatingDao<SubjectRatingEntity> getSubjectRatingDao();

    UserDao getUserDao();
}
