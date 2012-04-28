package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.jpa.JpaDaoFactory;

/**
 *
 * @author healarconr
 */
public abstract class AbstractDaoFactory {

    public static String JPA = "JPA";
    
    public static AbstractDaoFactory getDaoFactory(DataAccessAdapter da) {
        AbstractDaoFactory daoFactory = null;

        if (da.getType() == AbstractDaoFactory.JPA) {
            daoFactory = JpaDaoFactory.getInstance();
        }
        return daoFactory;
    }

    public abstract CommentDao getCommentDao();

    public abstract CommentRatingDao getCommentRatingDao();

    public abstract ConfirmationKeyDao getConfirmationKeyDao();

    public abstract CourseDao getCourseDao();

    public abstract CourseRatingDao getCourseRatingDao();

    public abstract FeedbackDao getFeedbackDao();

    public abstract FeedbackTypeDao getFeedbackTypeDao();

    public abstract PeriodDao getPeriodDao();

    public abstract ProfessorDao getProfessorDao();

    public abstract ProfessorRatingDao getProfessorRatingDao();

    public abstract ProgramDao getProgramDao();

    public abstract SubjectDao getSubjectDao();

    public abstract SubjectRatingDao getSubjectRatingDao();

    public abstract UserDao getUserDao();
}
