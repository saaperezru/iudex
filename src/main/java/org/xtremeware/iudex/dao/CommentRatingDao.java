/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingDao extends Dao<CommentRatingEntity> {
    
    /**
     * Returns a list of CommentRating entities given the comment
     * 
     * @param em the entity manager
     * @param commentId comment id
     * @return list of CommentRatingEntity
     */
    public List<CommentRatingEntity> getByCommentId(EntityManager em, Long commentId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createQuery("getByCommentId").setParameter("commentId", commentId).getResultList();
    }
    /**
     * Returns a rating given by a user, identified by userId, to a comment, identified by commentId.
     * 
     * @param em the entity manager
     * @param subjectId comment id
     * @param userId user id
     * @return a CommentRatingEntity
     */
    public CommentRatingEntity getByCommentIdAndUserId(EntityManager em, Long commentId, Long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try{
            return (CommentRatingEntity) em.createQuery("getByCommentIdAndUserId").setParameter("commentId", commentId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
   /**
     * Returns a summary of the ratings given a comment. 
     * 
     * @param em the entity manager
     * @param subjectId comment id
     * @return a RatingSummaryVo object
     */
    public RatingSummaryVo getSummary(EntityManager em, Long commentId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        RatingSummaryVo rsv = new RatingSummaryVo();

        Query q = em.createQuery("SELECT COUNT result FROM CommentRating result "
                + "WHERE result.comment.id = :commentId AND result.value = 1").setParameter("commentId", commentId);
        try{
            rsv.setPositive(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        q = em.createQuery("SELECT COUNT result FROM CommentRating result "
                + "WHERE result.comment.id = :commentId AND result.value = -1").setParameter("commentId", commentId);
        try{
            rsv.setNegative(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
