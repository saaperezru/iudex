package org.xtremeware.iudex.dao.test;

import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.dao.sql.ForgottenPasswordKeyDao;
import org.xtremeware.iudex.dao.sql.SQLCommentDao;
import org.xtremeware.iudex.dao.sql.SQLCommentRatingDao;
import org.xtremeware.iudex.dao.sql.SQLConfirmationKeyDao;
import org.xtremeware.iudex.dao.sql.SQLCourseDao;
import org.xtremeware.iudex.dao.sql.SQLCourseRatingDao;
import org.xtremeware.iudex.dao.sql.SQLFeedbackDao;
import org.xtremeware.iudex.dao.sql.SQLFeedbackTypeDao;
import org.xtremeware.iudex.dao.sql.SQLPeriodDao;
import org.xtremeware.iudex.dao.sql.SQLProfessorDao;
import org.xtremeware.iudex.dao.sql.SQLProfessorRatingDao;
import org.xtremeware.iudex.dao.sql.SQLProgramDao;
import org.xtremeware.iudex.dao.sql.SQLSubjectDao;
import org.xtremeware.iudex.dao.sql.SQLSubjectRatingDao;
import org.xtremeware.iudex.dao.sql.SQLUserDao;
import org.xtremeware.iudex.dao.sql.removeimplementations.*;
import org.xtremeware.iudex.entity.*;

/**
 * DAO factory for an HSQLDB persistence unit
 *
 * @author healarconr
 */
public class H2DaoBuilder implements AbstractDaoBuilder {

    private CommentDao commentDao;
    private BinaryRatingDao<CommentRatingEntity> commentRatingDao;
    private ConfirmationKeyDao confirmationKeyDao;
    private CourseDao courseDao;
    private CourseRatingDao courseRatingDao;
    private FeedbackDao feedbackDao;
    private FeedbackTypeDao feedbackTypeDao;
	private ForgottenPasswordKeyDaoInterface forgottenPasswordKeyDao;
    private PeriodDao periodDao;
    private ProfessorDao professorDao;
    private BinaryRatingDao<ProfessorRatingEntity> professorRatingDao;
    private ProgramDao programDao;
    private SubjectDao subjectDao;
    private BinaryRatingDao<SubjectRatingEntity> subjectRatingDao;
    private UserDao userDao;
    private static H2DaoBuilder instance;

    public H2DaoBuilder() {
    }


    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new SQLCommentDao(
                    new CommentsRemoveBehavior(
                    this, new SimpleRemoveBehavior<CommentEntity>()));
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
            confirmationKeyDao = new SQLConfirmationKeyDao(
                    new SimpleRemoveBehavior<ConfirmationKeyEntity>());
        }
        return confirmationKeyDao;
    }

    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new SQLCourseDao(
                    new CoursesRemoveBehavior(
                    this, new SimpleRemoveBehavior<CourseEntity>()));
        }
        return courseDao;
    }

    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new SQLCourseRatingDao(
                    new SimpleRemoveBehavior<CourseRatingEntity>());
        }
        return courseRatingDao;
    }

    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new SQLFeedbackDao(
                    new SimpleRemoveBehavior<FeedbackEntity>());
        }
        return feedbackDao;
    }

    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new SQLFeedbackTypeDao(
                    new SimpleRemoveBehavior<FeedbackTypeEntity>());
        }
        return feedbackTypeDao;
    }
	
	@Override
    public ForgottenPasswordKeyDaoInterface getForgottenPasswordKeyDao() {
        if (forgottenPasswordKeyDao == null) {
            forgottenPasswordKeyDao = new ForgottenPasswordKeyDao(new SimpleRemoveBehavior<ForgottenPasswordKeyEntity>());
        }
        return forgottenPasswordKeyDao;
    }


    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new SQLPeriodDao(
                    new PeriodRemoveBehavior(
                    this, new SimpleRemoveBehavior<PeriodEntity>()));
        }
        return periodDao;
    }

    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new SQLProfessorDao(
                    new ProfessorsRemoveBehavior(
                    this, new SimpleRemoveBehavior<ProfessorEntity>()));
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
            programDao = new SQLProgramDao(
                    new SimpleRemoveBehavior<ProgramEntity>());
        }
        return programDao;
    }

    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SQLSubjectDao(
                    new SubjectsRemoveBehavior(
                    this, new SimpleRemoveBehavior<SubjectEntity>()));
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
            userDao = new SQLUserDao(
                    new UsersRemoveBehavior(
                    this, new SimpleRemoveBehavior<UserEntity>()));
        }
        return userDao;
    }
}
