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
 *
 * @author healarconr
 */
public interface AbstractDaoFactory {

    public JpaCommentDao getCommentDao();

    public JpaCommentRatingDao getCommentRatingDao();

    public JpaConfirmationKeyDao getConfirmationKeyDao();

    public JpaCourseDao getCourseDao();

    public JpaCourseRatingDao getCourseRatingDao();

    public JpaFeedbackDao getFeedbackDao();

    public JpaFeedbackTypeDao getFeedbackTypeDao();

    public JpaPeriodDao getPeriodDao();

    public JpaProfessorDao getProfessorDao();

    public JpaProfessorRatingDao getProfessorRatingDao();

    public JpaProgramDao getProgramDao();

    public JpaSubjectDao getSubjectDao();

    public JpaSubjectRatingDao getSubjectRatingDao();

    public JpaUserDao getUserDao();
}
