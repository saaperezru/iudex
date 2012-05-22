package org.xtremeware.iudex.dao.sql;

import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.BinaryRatingDao;
import org.xtremeware.iudex.dao.CommentDao;
import org.xtremeware.iudex.dao.ConfirmationKeyDao;
import org.xtremeware.iudex.dao.CourseDao;
import org.xtremeware.iudex.dao.CourseRatingDao;
import org.xtremeware.iudex.dao.FeedbackDao;
import org.xtremeware.iudex.dao.FeedbackTypeDao;
import org.xtremeware.iudex.dao.PeriodDao;
import org.xtremeware.iudex.dao.ProfessorDao;
import org.xtremeware.iudex.dao.ProgramDao;
import org.xtremeware.iudex.dao.SubjectDao;
import org.xtremeware.iudex.dao.UserDao;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 * DAO factory for an HSQLDB persistence unit
 *
 * @author healarconr
 */
public class HSqlDbDaoFactory implements AbstractDaoFactory {

    private CommentDao commentDao;
    private BinaryRatingDao<CommentRatingEntity> commentRatingDao;
    private ConfirmationKeyDao confirmationKeyDao;
    private CourseDao courseDao;
    private CourseRatingDao courseRatingDao;
    private FeedbackDao feedbackDao;
    private FeedbackTypeDao feedbackTypeDao;
    private PeriodDao periodDao;
    private ProfessorDao professorDao;
    private BinaryRatingDao<ProfessorRatingEntity> professorRatingDao;
    private ProgramDao programDao;
    private SubjectDao subjectDao;
    private BinaryRatingDao<SubjectRatingEntity> subjectRatingDao;
    private UserDao userDao;
    private static HSqlDbDaoFactory instance;

    private HSqlDbDaoFactory() {
    }

    public static synchronized HSqlDbDaoFactory getInstance() {
        if (instance == null) {
            instance = new HSqlDbDaoFactory();
        }
        return instance;
    }

    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new SQLCommentDao();
        }
        return commentDao;
    }

    @Override
    public BinaryRatingDao<CommentRatingEntity> getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new SQLCommentRatingDao();
        }
        return commentRatingDao;
    }

    @Override
    public ConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new SQLConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }

    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new SQLCourseDao();
        }
        return courseDao;
    }

    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new SQLCourseRatingDao();
        }
        return courseRatingDao;
    }

    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new SQLFeedbackDao();
        }
        return feedbackDao;
    }

    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new SQLFeedbackTypeDao();
        }
        return feedbackTypeDao;
    }

    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new SQLPeriodDao();
        }
        return periodDao;
    }

    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new SQLProfessorDao();
        }
        return professorDao;
    }

    @Override
    public BinaryRatingDao<ProfessorRatingEntity> getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new SQLProfessorRatingDao();
        }
        return professorRatingDao;
    }

    @Override
    public ProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new SQLProgramDao();
        }
        return programDao;
    }

    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SQLSubjectDao();
        }
        return subjectDao;
    }

    @Override
    public BinaryRatingDao<SubjectRatingEntity> getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new SQLSubjectRatingDao();
        }
        return subjectRatingDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new SQLUserDao();
        }
        return userDao;
    }
}
