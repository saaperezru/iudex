/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CommentRatingDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingsService extends SimpleCrudService<CommentRatingVo, CommentRatingEntity> {

    /**
     * CommentRatingsService constructor
     *
     * @param daoFactory
     */
    public CommentRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaCommentRatingDao to be used.
     *
     * @return
     */
    @Override
    protected CrudDao<CommentRatingVo, CommentRatingEntity> getDao() {
        return this.getDaoFactory().getCommentRatingDao();
    }

    /**
     * Validate the provided CommentRatingVo, if the CommentRatingVo is not
     * correct the methods throws an exception
     *
     * @param em DataAccessAdapter
     * @param vo CommentRatingVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(DataAccessAdapter em, CommentRatingVo vo) throws InvalidVoException, DataAccessException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null CommentRatingVo");
        }
        if (vo.getEvaluetedObjectId() == null) {
            throw new InvalidVoException("Null commentId in the provided CourseRatingVo");
        }
        if (getDaoFactory().getCommentDao().getById(em, vo.getEvaluetedObjectId()) == null) {
            throw new InvalidVoException("No such comment associeted with CommentRatingVo.commentId");
        }
        if (vo.getUser() == null) {
            throw new InvalidVoException("Null userId in the provided CommentRatingVo");
        }
        if (getDaoFactory().getUserDao().getById(em, vo.getUser()) == null) {
            throw new InvalidVoException("No such user associated with CommentRatingVo.userId");
        }
        if (vo.getValue() < -1 || vo.getValue() > 1) {
            throw new InvalidVoException("int Value in the provided CommentRatingVo must be less than or equal to 1 and greater than or equal to -1");
        }
    }

    /**
     * Returns a CommentRatingVo associated with the provided userId and
     * courseId
     *
     * @param em EntityManager
     * @param commentId comment identifier
     * @param userId user identifier
     * @return CommentRatingVo
     */
    public CommentRatingVo getByCommentIdAndUserId(DataAccessAdapter em, long commentId, long userId) throws DataAccessException {
        return ((CommentRatingDao) this.getDao()).getByCommentIdAndUserId(em, commentId, userId);
    }

    /**
     * Returns a summary of the rating, given a comment.
     *
     * @param em EntityManager
     * @param commentId comment identifier
     * @return RatingSummaryVo
     */
    public RatingSummaryVo getByCommentId(DataAccessAdapter em, long commentId) throws DataAccessException {
        return ((CommentRatingDao) this.getDao()).getSummary(em, commentId);
    }

    /**
     * Returns a summary of the ratings associated with a specified comment.
     *
     * @param em the entity manager
     * @param commentId
     * @return a RatingSummaryVo object with the information associated with the
     * ratings corresponding to the specified comment
     */
    public RatingSummaryVo getSummary(DataAccessAdapter em, long commentId) throws DataAccessException {
        return ((CommentRatingDao) getDao()).getSummary(em, commentId);
    }
}
