package org.xtremeware.iudex.dao;

/**
 *
 * @author healarconr
 */
public interface AbstractDaoFactory {

    public CommentDaoInterface getCommentDao();

    public CommentRatingDaoInterface getCommentRatingDao();

    public ConfirmationKeyDaoInterface getConfirmationKeyDao();

    public CourseDaoInterface getCourseDao();

    public CourseRatingDaoInterface getCourseRatingDao();

    public FeedbackDaoInterface getFeedbackDao();

    public FeedbackTypeDaoInterface getFeedbackTypeDao();

    public ForgottenPasswordKeyDaoInterface getForgottenPasswordKeyDao();
    
    public PeriodDaoInterface getPeriodDao();

    public ProfessorDaoInterface getProfessorDao();

    public ProfessorRatingDaoInterface getProfessorRatingDao();

    public ProgramDaoInterface getProgramDao();

    public SubjectDaoInterface getSubjectDao();

    public SubjectRatingDaoInterface getSubjectRatingDao();

    public UserDaoInterface getUserDao();
}
