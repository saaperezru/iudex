package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 *
 * @author healarconr
 */
public interface AbstractDaoFactory {

    public CommentDaoInterface getCommentDao();

    public BinaryRatingDaoInterface<CommentRatingEntity> getCommentRatingDao();

    public ConfirmationKeyDaoInterface getConfirmationKeyDao();

    public CourseDaoInterface getCourseDao();

    public CourseRatingDaoInterface getCourseRatingDao();

    public FeedbackDaoInterface getFeedbackDao();

    public FeedbackTypeDaoInterface getFeedbackTypeDao();

    public PeriodDaoInterface getPeriodDao();

    public ProfessorDaoInterface getProfessorDao();

    public BinaryRatingDaoInterface<ProfessorRatingEntity> getProfessorRatingDao();

    public ProgramDaoInterface getProgramDao();

    public SubjectDaoInterface getSubjectDao();

    public BinaryRatingDaoInterface<SubjectRatingEntity> getSubjectRatingDao();

    public UserDaoInterface getUserDao();
}
