package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.dao.jpa.JpaCommentDao;
import org.xtremeware.iudex.dao.jpa.JpaCommentRatingDao;
import org.xtremeware.iudex.dao.jpa.JpaConfirmationKeyDao;
import org.xtremeware.iudex.dao.jpa.JpaCourseDao;
import org.xtremeware.iudex.dao.jpa.JpaCourseRatingDao;
import org.xtremeware.iudex.dao.jpa.JpaFeedbackDao;
import org.xtremeware.iudex.dao.jpa.JpaFeedbackTypeDao;
import org.xtremeware.iudex.dao.jpa.JpaPeriodDao;
import org.xtremeware.iudex.dao.jpa.JpaProfessorDao;
import org.xtremeware.iudex.dao.jpa.JpaProfessorRatingDao;
import org.xtremeware.iudex.dao.jpa.JpaProgramDao;
import org.xtremeware.iudex.dao.jpa.JpaSubjectDao;
import org.xtremeware.iudex.dao.jpa.JpaSubjectRatingDao;
import org.xtremeware.iudex.dao.jpa.JpaUserDao;

/**
 * DAO factory for a MySQL persistence unit
 * 
 * @author healarconr
 */
public class MySqlDaoFactory implements AbstractDaoFactory {
    
    private JpaCommentDao commentDao;
    private JpaCommentRatingDao commentRatingDao;
    private JpaConfirmationKeyDao confirmationKeyDao;
    private JpaCourseDao courseDao;
    private JpaCourseRatingDao courseRatingDao;
    private JpaFeedbackDao feedbackDao;
    private JpaFeedbackTypeDao feedbackTypeDao;
    private JpaPeriodDao periodDao;
    private JpaProfessorDao professorDao;
    private JpaProfessorRatingDao professorRatingDao;
    private JpaProgramDao programDao;
    private JpaSubjectDao subjectDao;
    private JpaSubjectRatingDao subjectRatingDao;
    private JpaUserDao userDao;
    
    @Override
    public JpaCommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new JpaCommentDao();
        }
        return commentDao;
    }
    
    @Override
    public JpaCommentRatingDao getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new JpaCommentRatingDao();
        }
        return commentRatingDao;
    }
    
    @Override
    public JpaConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new JpaConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }
    
    @Override
    public JpaCourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new JpaCourseDao();
        }
        return courseDao;
    }
    
    @Override
    public JpaCourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new JpaCourseRatingDao();
        }
        return courseRatingDao;
    }
    
    @Override
    public JpaFeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new JpaFeedbackDao();
        }
        return feedbackDao;
    }
    
    @Override
    public JpaFeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new JpaFeedbackTypeDao();
        }
        return feedbackTypeDao;
    }
    
    @Override
    public JpaPeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new JpaPeriodDao();
        }
        return periodDao;
    }
    
    @Override
    public JpaProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new JpaProfessorDao();
        }
        return professorDao;
    }
    
    @Override
    public JpaProfessorRatingDao getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new JpaProfessorRatingDao();
        }
        return professorRatingDao;
    }
    
    @Override
    public JpaProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new JpaProgramDao();
        }
        return programDao;
    }
    
    @Override
    public JpaSubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new JpaSubjectDao();
        }
        return subjectDao;
    }
    
    @Override
    public JpaSubjectRatingDao getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new JpaSubjectRatingDao();
        }
        return subjectRatingDao;
    }
    
    @Override
    public JpaUserDao getUserDao() {
        if (userDao == null) {
            userDao = new JpaUserDao();
        }
        return userDao;
    }
}
