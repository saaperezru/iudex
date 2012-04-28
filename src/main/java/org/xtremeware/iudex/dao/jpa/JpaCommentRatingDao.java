/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.CommentRatingDao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * JpaDao for CommentRating value objects. Implements additionally some useful
 * finders by associated comment id and user id.
 *
 * @author josebermeo
 */
public class JpaCommentRatingDao extends JpaCrudDao<CommentRatingVo, CommentRatingEntity> implements CommentRatingDao<EntityManager> {

    /**
     * Returns a CommentRating entity using the information in the provided
     * CommentRating value object.
     * 
     * @param em the data access adapter
     * @param vo the CommentRating value object
     * @return the CommentRating entity
     */
    @Override
    protected CommentRatingEntity voToEntity(DataAccessAdapter<EntityManager> em, CommentRatingVo vo) {

        CommentRatingEntity commentRatingEntity = new CommentRatingEntity();
        
        commentRatingEntity.setId(vo.getId());
        commentRatingEntity.setValue(vo.getValue());

        commentRatingEntity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUser()));
        commentRatingEntity.setComment(em.getDataAccess().getReference(CommentEntity.class, vo.getEvaluetedObjectId()));
        
        return commentRatingEntity;
    }

    @Override
    protected Class getEntityClass() {
        return CommentRatingEntity.class;
    }

    /**
     * Returns a list of CommentRating entities given the comment
     *
     * @param em the data access adapter
     * @param commentId comment id
     * @return list of CommentRatingVo
     */
    @Override
    public List<CommentRatingVo> getByCommentId(DataAccessAdapter<EntityManager> em, Long commentId) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentRatingByCommentId").setParameter("commentId", commentId).getResultList());
    }

    /**
     * Returns a rating given by a user, identified by userId, to a comment,
     * identified by commentId.
     *
     * @param em the data access adapter
     * @param commentId comment id
     * @param userId
     * @return a CommentRatingVo
     */
    @Override
    public CommentRatingVo getByCommentIdAndUserId(DataAccessAdapter<EntityManager> em, Long commentId, Long userId) {
        checkDataAccessAdapter(em);
        try {
            return ((CommentRatingEntity) em.getDataAccess().createNamedQuery("getCommentRatingByCommentIdAndUserId").setParameter("commentId", commentId).setParameter("userId", userId).getSingleResult()).toVo();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * Returns a list of CommentRatingVo given the user, identified by userId
     *
     * @param em the data access adapter
     * @param userId user id
     * @return list of CommentRatingVo
     */
    @Override
    public List<CommentRatingVo> getByUserId(DataAccessAdapter<EntityManager> em, Long userId) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentRatingByUserId").setParameter("userId", userId).getResultList());
    }

    /**
     * Returns a summary of the ratings given a comment.
     *
     * @param em the data acces adapter
     * @param commentId comment id
     * @return a RatingSummaryVo object, null if the COUNTING process of either
     * positive or negative ratings returns no result from the EntityManager.
     */
    @Override
    public RatingSummaryVo getSummary(DataAccessAdapter<EntityManager> em, Long commentId) {
        checkDataAccessAdapter(em);

        RatingSummaryVo rsv = new RatingSummaryVo();

        Query q = em.getDataAccess().createNamedQuery("countPositiveCommentRating").setParameter("commentId", commentId);
        try {
            rsv.setPositive(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        q = em.getDataAccess().createNamedQuery("countNegativeCommentRating").setParameter("commentId", commentId);
        try {
            rsv.setNegative(((Integer) q.getSingleResult()).intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }
}
