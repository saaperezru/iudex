/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CommentRatingDao;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingService extends SimpleCrudService<CommentRatingVo, CommentRatingEntity> {
    /**
     * CommentRatingService constructor
     * 
     * @param daoFactory 
     */
    public CommentRatingService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }
    /**
     * returns the CommentRatingDao to be used.
     * 
     * @return 
     */
    @Override
    protected Dao<CommentRatingEntity> getDao() {
        return this.getDaoFactory().getCommentRatingDao();
    }
    /**
     * Validate the provided CommentRatingVo, if the CommentRatingVo is not
     * correct the methods throws an exception
     * 
     * @param em EntityManager
     * @param vo CommentRatingVo
     * @throws InvalidVoException 
     */
    @Override
    public void validateVo(EntityManager em, CommentRatingVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null CommentRatingVo");
        }
        if (vo.getCommentId() == null) {
            throw new InvalidVoException("Null commentId in the provided CourseRatingVo");
        }
        if (getDaoFactory().getCommentDao().getById(em, vo.getCommentId()) == null) {
            throw new InvalidVoException("No such comment associeted with CommentRatingVo.commentId");
        }
        if (vo.getUserId() == null) {
            throw new InvalidVoException("Null userId in the provided CommentRatingVo");
        }
        if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) == null) {
            throw new InvalidVoException("No such user associated with CommentRatingVo.userId");
        }
        if (vo.getValue() < -1 || vo.getValue() > 1) {
            throw new InvalidVoException("int Value in the provided CommentRatingVo must be less than or equal to 1 and greater than or equal to -1");
        }
    }
    /**
     * Returns a CommentRatingEntity using the information in the provided CommentRatingVo.
     * 
     * @param em EntityManager
     * @param vo CommentRatingVo
     * @return CommentRatingEntity
     * @throws InvalidVoException 
     */
    @Override
    public CommentRatingEntity voToEntity(EntityManager em, CommentRatingVo vo) throws InvalidVoException {
        
        validateVo(em, vo);
        
        CommentRatingEntity commentRatingEntity = new CommentRatingEntity();
        commentRatingEntity.setId(vo.getId());
        commentRatingEntity.setValue(vo.getValue());
        
        commentRatingEntity.setUser(this.getDaoFactory().getUserDao().getById(em, vo.getUserId()));
        commentRatingEntity.setComment(this.getDaoFactory().getCommentDao().getById(em, vo.getCommentId()));
        
        return commentRatingEntity;
    }
    /**
     * Returns a CommentRatingVo associated with the provided userId and courseId
     * 
     * @param em EntityManager
     * @param commentId comment identifier
     * @param userId user identifier
     * @return CommentRatingVo
     */
    public CommentRatingVo getByCommentIdAndUserId(EntityManager em, long commentId, long userId) {
        CommentRatingEntity commentRatingEntity = ((CommentRatingDao)this.getDao()).getByCommentIdAndUserId(em, commentId, userId);
        if(commentRatingEntity == null){
            return null;
        }
        return commentRatingEntity.toVo();
    }
    /**
     * Returns a summary of the rating, given a comment. 
     * 
     * @param em EntityManager
     * @param commentId comment identifier
     * @return RatingSummaryVo
     */
    public RatingSummaryVo getByCommentId(EntityManager em, long commentId) {
        return ((CommentRatingDao)this.getDao()).getSummary(em, commentId);
    }
}
