package org.xtremeware.iudex.dao;

/**
 * DAO factory for an HSQLDB persistence unit
 *
 * @author healarconr
 */
public class HSqlDbDaoFactory implements AbstractDaoFactory {

    private CommentDaoInterface commentDao;
    private CommentRatingDaoInterface commentRatingDao;
    private ConfirmationKeyDaoInterface confirmationKeyDao;
    private CourseDaoInterface courseDao;
    private CourseRatingDaoInterface courseRatingDao;
    private FeedbackDaoInterface feedbackDao;
    private FeedbackTypeDaoInterface feedbackTypeDao;
    private PeriodDaoInterface periodDao;
    private ProfessorDaoInterface professorDao;
    private ProfessorRatingDaoInterface professorRatingDao;
    private ProgramDaoInterface programDao;
    private SubjectDaoInterface subjectDao;
    private SubjectRatingDaoInterface subjectRatingDao;
    private UserDaoInterface userDao;

    @Override
    public CommentDaoInterface getCommentDao() {
        if (commentDao == null) {
            commentDao = new CommentDao();
        }
        return commentDao;
    }

    @Override
    public CommentRatingDaoInterface getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new CommentRatingDao();
        }
        return commentRatingDao;
    }

    @Override
    public ConfirmationKeyDaoInterface getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new ConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }

    @Override
    public CourseDaoInterface getCourseDao() {
        if (courseDao == null) {
            courseDao = new CourseDao();
        }
        return courseDao;
    }

    @Override
    public CourseRatingDaoInterface getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new CourseRatingDao();
        }
        return courseRatingDao;
    }

    @Override
    public FeedbackDaoInterface getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new FeedbackDao();
        }
        return feedbackDao;
    }

    @Override
    public FeedbackTypeDaoInterface getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new FeedbackTypeDao();
        }
        return feedbackTypeDao;
    }

    @Override
    public PeriodDaoInterface getPeriodDao() {
        if (periodDao == null) {
            periodDao = new PeriodDao();
        }
        return periodDao;
    }

    @Override
    public ProfessorDaoInterface getProfessorDao() {
        if (professorDao == null) {
            professorDao = new ProfessorDao();
        }
        return professorDao;
    }

    @Override
    public ProfessorRatingDaoInterface getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new ProfessorRatingDao();
        }
        return professorRatingDao;
    }

    @Override
    public ProgramDaoInterface getProgramDao() {
        if (programDao == null) {
            programDao = new ProgramDao();
        }
        return programDao;
    }

    @Override
    public SubjectDaoInterface getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SubjectDao();
        }
        return subjectDao;
    }

    @Override
    public SubjectRatingDaoInterface getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new SubjectRatingDao();
        }
        return subjectRatingDao;
    }

    @Override
    public UserDaoInterface getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
}
