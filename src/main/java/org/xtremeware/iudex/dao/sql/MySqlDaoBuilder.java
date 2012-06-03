package org.xtremeware.iudex.dao.sql;

import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.dao.sql.deleteimplementations.*;
import org.xtremeware.iudex.entity.*;

/**
 * DAO factory for a MySql persistence unit
 *
 * @author healarconr
 */
public final class MySqlDaoBuilder implements AbstractDaoBuilder {

    private CommentDao commentDao;
    private BinaryRatingDao<CommentRatingEntity> commentRatingDao;
    private ConfirmationKeyDao confirmationKeyDao;
    private CourseDao courseDao;
    private CourseRatingDao courseRatingDao;
    private FeedbackDao feedbackDao;
    private FeedbackTypeDao feedbackTypeDao;
    private ForgottenPasswordKeyDao forgottenPasswordKeyDao;
    private PeriodDao periodDao;
    private ProfessorDao professorDao;
    private BinaryRatingDao<ProfessorRatingEntity> professorRatingDao;
    private ProgramDao programDao;
    private SubjectDao subjectDao;
    private BinaryRatingDao<SubjectRatingEntity> subjectRatingDao;
    private UserDao userDao;

    public MySqlDaoBuilder() {
        try {
            Class.forName("com.mySql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new SqlCommentDao(
                    new CommentsDeleteBehavior(
                    this, new SimpleDeleteBehavior<CommentEntity>()));
        }
        return commentDao;
    }

    @Override
    public BinaryRatingDao<CommentRatingEntity> getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new SqlCommentRatingDao();
        }
        return commentRatingDao;
    }

    @Override
    public ConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new SqlConfirmationKeyDao(
                    new SimpleDeleteBehavior<ConfirmationKeyEntity>());
        }
        return confirmationKeyDao;
    }

    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new SqlCourseDao(
                    new CoursesDeleteBehavior(
                    this, new SimpleDeleteBehavior<CourseEntity>()));
        }
        return courseDao;
    }

    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new SqlCourseRatingDao(
                    new SimpleDeleteBehavior<CourseRatingEntity>());
        }
        return courseRatingDao;
    }

    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new SqlFeedbackDao(
                    new SimpleDeleteBehavior<FeedbackEntity>());
        }
        return feedbackDao;
    }

    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new SqlFeedbackTypeDao(
                    new SimpleDeleteBehavior<FeedbackTypeEntity>());
        }
        return feedbackTypeDao;
    }

    @Override
    public ForgottenPasswordKeyDao getForgottenPasswordKeyDao() {
        if (forgottenPasswordKeyDao == null) {
            forgottenPasswordKeyDao = new SqlForgottenPasswordKeyDao(new SimpleDeleteBehavior<ForgottenPasswordKeyEntity>());
        }
        return forgottenPasswordKeyDao;
    }

    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new SqlPeriodDao(
                    new PeriodDeleteBehavior(
                    this, new SimpleDeleteBehavior<PeriodEntity>()));
        }
        return periodDao;
    }

    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new SqlProfessorDao(
                    new ProfessorsDeleteBehavior(
                    this, new SimpleDeleteBehavior<ProfessorEntity>()));
        }
        return professorDao;
    }

    @Override
    public BinaryRatingDao<ProfessorRatingEntity> getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new SqlProfessorRatingDao();
        }
        return professorRatingDao;
    }

    @Override
    public ProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new SqlProgramDao(
                    new SimpleDeleteBehavior<ProgramEntity>());
        }
        return programDao;
    }

    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SqlSubjectDao(
                    new SubjectsDeleteBehavior(
                    this, new SimpleDeleteBehavior<SubjectEntity>()));
        }
        return subjectDao;
    }

    @Override
    public BinaryRatingDao<SubjectRatingEntity> getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new SqlSubjectRatingDao();
        }
        return subjectRatingDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new SqlUserDao(
                    new UsersDeleteBehavior(
                    this, new SimpleDeleteBehavior<UserEntity>()));
        }
        return userDao;
    }
}
