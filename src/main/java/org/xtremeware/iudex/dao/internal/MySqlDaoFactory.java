package org.xtremeware.iudex.dao.internal;

import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 * DAO factory for a MySQL persistence unit
 *
 * @author healarconr
 */
public class MySqlDaoFactory implements AbstractDaoFactory {

    private CommentDaoInterface commentDao;
    private BinaryRatingDaoInterface<CommentRatingEntity> commentRatingDao;
    private ConfirmationKeyDaoInterface confirmationKeyDao;
    private CourseDaoInterface courseDao;
    private CourseRatingDaoInterface courseRatingDao;
    private FeedbackDaoInterface feedbackDao;
    private FeedbackTypeDaoInterface feedbackTypeDao;
    private PeriodDaoInterface periodDao;
    private ProfessorDaoInterface professorDao;
    private BinaryRatingDaoInterface<ProfessorRatingEntity> professorRatingDao;
    private ProgramDaoInterface programDao;
    private SubjectDaoInterface subjectDao;
    private BinaryRatingDaoInterface<SubjectRatingEntity> subjectRatingDao;
    private UserDaoInterface userDao;
    private static MySqlDaoFactory instance;

    private MySqlDaoFactory() {
    }

    public static synchronized MySqlDaoFactory getInstance() {
        if (instance == null) {
            instance = new MySqlDaoFactory();
        }
        return instance;
    }

    @Override
    public CommentDaoInterface getCommentDao() {
        if (commentDao == null) {
            commentDao = new CommentDao();
        }
        return commentDao;
    }

    @Override
    public BinaryRatingDaoInterface<CommentRatingEntity> getCommentRatingDao() {
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
    public BinaryRatingDaoInterface<ProfessorRatingEntity> getProfessorRatingDao() {
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
    public BinaryRatingDaoInterface<SubjectRatingEntity> getSubjectRatingDao() {
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
