/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao.jpa;

import org.xtremeware.iudex.dao.*;

/**
 *
 * @author josebermeo
 */
public class JpaDaoFactory extends AbstractDaoFactory{
    
    private static JpaDaoFactory instance = null;
    
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
    
    public synchronized static JpaDaoFactory getInstance() {
        if (instance == null) {
            instance = new JpaDaoFactory();
        }
        return instance;
    }
    
    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new JpaCommentDao();
        }
        return commentDao;
    }
    
    @Override
    public CommentRatingDao getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new JpaCommentRatingDao();
        }
        return commentRatingDao;
    }
    
    @Override
    public ConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new JpaConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }
    
    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new JpaCourseDao();
        }
        return courseDao;
    }
    
    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new JpaCourseRatingDao();
        }
        return courseRatingDao;
    }
    
    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new JpaFeedbackDao();
        }
        return feedbackDao;
    }
    
    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new JpaFeedbackTypeDao();
        }
        return feedbackTypeDao;
    }
    
    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new JpaPeriodDao();
        }
        return periodDao;
    }
    
    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new JpaProfessorDao();
        }
        return professorDao;
    }
    
    @Override
    public ProfessorRatingDao getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new JpaProfessorRatingDao();
        }
        return professorRatingDao;
    }
    
    @Override
    public ProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new JpaProgramDao();
        }
        return programDao;
    }
    
    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new JpaSubjectDao();
        }
        return subjectDao;
    }
    
    @Override
    public SubjectRatingDao getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new JpaSubjectRatingDao();
        }
        return subjectRatingDao;
    }
    
    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new JpaUserDao();
        }
        return userDao;
    }
    
}
