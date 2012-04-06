package org.xtremeware.iudex.dao;

/**
 *
 * @author healarconr
 */
public interface AbstractDaoFactory {

    public CommentDao getCommentDao();

    public CommentRatingDao getCommentRatingDao();

    public ConfirmationKeyDao getConfirmationKeyDao();

    public CourseDao getCourseDao();

    public CourseRatingDao getCourseRatingDao();

    public FeedbackDao getFeedbackDao();

    public FeedbackTypeDao getFeedbackTypeDao();

    public PeriodDao getPeriodDao();

    public ProfessorDao getProfessorDao();

    public ProfessorRatingDao getProfessorRatingDao();

    public ProgramDao getProgramDao();

    public SubjectDao getSubjectDao();

    public SubjectRatingDao getSubjectRatingDao();

    public UserDao getUserDao();
}
