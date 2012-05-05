package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingDao extends CrudDao<CommentRatingEntity> implements CommentRatingDaoInterface {

    /**
     * Returns a list of CommentRating entities given the comment
     *
     * @param em the entity manager
     * @param commentId comment id
     * @return list of CommentRatingEntity
     */
    @Override
    public List<CommentRatingEntity> getByCommentId(EntityManager em, Long commentId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentRatingByCommentId", CommentRatingEntity.class).setParameter("commentId", commentId).getResultList();
    }

    /**
     * Returns a rating given by a user, identified by userId, to a comment,
     * identified by commentId.
     *
     * @param em the entity manager
     * @param subjectId comment id
     * @param userId user id
     * @return a CommentRatingEntity
     */
    @Override
    public CommentRatingEntity getByCommentIdAndUserId(EntityManager em, Long commentId, Long userId) {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCommentRatingByCommentIdAndUserId", CommentRatingEntity.class).setParameter("commentId", commentId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * Returns a list of CommentRating entities given the user
     *
     * @param em the entity manager
     * @param userId user id
     * @return list of CommentRatingEntity
     */
    @Override
    public List<CommentRatingEntity> getByUserId(EntityManager em, Long userId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentRatingByUserId", CommentRatingEntity.class).setParameter("userId", userId).getResultList();
    }

    /**
     * Returns a summary of the ratings given a comment.
     *
     * @param em the entity manager
     * @param subjectId comment id
     * @return a RatingSummaryVo object, null if the COUNTING process of either
     * positive or negative ratings returns no result from the EntityManager.
     */
    @Override
    public RatingSummaryVo getSummary(EntityManager em, Long commentId) {
        checkEntityManager(em);
        RatingSummaryVo rsv = new RatingSummaryVo();

        try {
            rsv.setPositive(em.createNamedQuery("countPositiveCommentRating", Long.class).setParameter("commentId", commentId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        try {
            rsv.setNegative(em.createNamedQuery("countNegativeCommentRating", Long.class).setParameter("commentId", commentId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
