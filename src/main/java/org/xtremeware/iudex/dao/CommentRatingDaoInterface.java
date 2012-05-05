package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO Interface for the CommentRating entities.
 *
 * @author josebermeo
 */
public interface CommentRatingDaoInterface extends CrudDaoInterface<CommentRatingEntity> {

    /**
     * Returns a list of CommentRating entities given the comment
     *
     * @param em the entity manager
     * @param commentId comment id
     * @return list of CommentRatingEntity
     */
    public List<CommentRatingEntity> getByCommentId(EntityManager em, Long commentId);

    /**
     * Returns a rating given by a user, identified by userId, to a comment,
     * identified by commentId.
     *
     * @param em the entity manager
     * @param subjectId comment id
     * @param userId user id
     * @return a CommentRatingEntity
     */
    public CommentRatingEntity getByCommentIdAndUserId(EntityManager em, Long commentId, Long userId);

    /**
     * Returns a list of CommentRating entities given the user
     *
     * @param em the entity manager
     * @param userId user id
     * @return list of CommentRatingEntity
     */
    public List<CommentRatingEntity> getByUserId(EntityManager em, Long userId);

    /**
     * Returns a summary of the ratings given a comment.
     *
     * @param em the entity manager
     * @param subjectId comment id
     * @return a RatingSummaryVo object, null if the COUNTING process of either
     * positive or negative ratings returns no result from the EntityManager.
     */
    public RatingSummaryVo getSummary(EntityManager em, Long commentId);
}
