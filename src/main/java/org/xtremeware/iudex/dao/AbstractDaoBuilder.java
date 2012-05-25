package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 *
 * @author healarconr
 */
public interface AbstractDaoBuilder {

    public CommentDao getCommentDao();

    public BinaryRatingDao<CommentRatingEntity> getCommentRatingDao();

    public ConfirmationKeyDao getConfirmationKeyDao();

    public CourseDao getCourseDao();

    public CourseRatingDao getCourseRatingDao();

    public FeedbackDao getFeedbackDao();

    public FeedbackTypeDao getFeedbackTypeDao();

    public PeriodDao getPeriodDao();

    public ProfessorDao getProfessorDao();

    public BinaryRatingDao<ProfessorRatingEntity> getProfessorRatingDao();

    public ProgramDao getProgramDao();

    public SubjectDao getSubjectDao();

    public BinaryRatingDao<SubjectRatingEntity> getSubjectRatingDao();

    public UserDao getUserDao();
}
