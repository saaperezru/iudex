package org.xtremeware.iudex.dao;

/**
 * DAO factory for an HSQLDB persistence unit
 * 
 * @author healarconr
 */
public class HSqlDbDaoFactory implements AbstractDaoFactory {
    
    private CommentDao commentDao;
    private CommentRatingDao commentRatingDao;
    private ConfirmationKeyDao confirmationKeyDao;
    private CourseDao courseDao;
    private CourseRatingDao courseRatingDao;
    private FeedbackDao feedbackDao;
    private FeedbackTypeDao feedbackTypeDao;
    private PeriodDao periodDao;
    private ProfessorDao professorDao;
    private ProfessorRatingDao professorRatingDao;
    private ProgramDao programDao;
    private SubjectDao subjectDao;
    private SubjectRatingDao subjectRatingDao;
    private UserDao userDao;
    
    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new CommentDao();
        }
        return commentDao;
    }
    
    @Override
    public CommentRatingDao getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new CommentRatingDao();
        }
        return commentRatingDao;
    }
    
    @Override
    public ConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new ConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }
    
    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new CourseDao();
        }
        return courseDao;
    }
    
    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new CourseRatingDao();
        }
        return courseRatingDao;
    }
    
    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new FeedbackDao();
        }
        return feedbackDao;
    }
    
    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new FeedbackTypeDao();
        }
        return feedbackTypeDao;
    }
    
    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new PeriodDao();
        }
        return periodDao;
    }
    
    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new ProfessorDao();
        }
        return professorDao;
    }
    
    @Override
    public ProfessorRatingDao getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new ProfessorRatingDao();
        }
        return professorRatingDao;
    }
    
    @Override
    public ProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new ProgramDao();
        }
        return programDao;
    }
    
    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SubjectDao();
        }
        return subjectDao;
    }
    
    @Override
    public SubjectRatingDao getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new SubjectRatingDao();
        }
        return subjectRatingDao;
    }
    
    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
}
