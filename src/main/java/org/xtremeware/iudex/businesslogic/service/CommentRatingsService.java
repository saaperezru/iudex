package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingsService extends CrudService<CommentRatingVo, CommentRatingEntity> {

    /**
     * CommentRatingsService constructor
     *
     * @param daoFactory
     */
    public CommentRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<CommentRatingEntity>(daoFactory.
                getCommentRatingDao()),
                new SimpleRead<CommentRatingEntity>(daoFactory.
                getCommentRatingDao()),
                new SimpleUpdate<CommentRatingEntity>(daoFactory.
                getCommentRatingDao()),
                new SimpleRemove<CommentRatingEntity>(daoFactory.
                getCommentRatingDao()));
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
    public void validateVo(EntityManager em, CommentRatingVo vo)
            throws MultipleMessagesException, DataBaseException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();

        if (vo == null) {
            multipleMessageException.addMessage(
                    "Null CommentRatingVo");
            throw multipleMessageException;
        }
        if (vo.getEvaluetedObjectId() == null) {
            multipleMessageException.addMessage(
                    "Null commentId in the provided CourseRatingVo");
        } else if (getDaoFactory().getCommentDao().getById(em, vo.
                getEvaluetedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "No such comment associeted with CommentRatingVo.commentId");
        }
        if (vo.getUser() == null) {
            multipleMessageException.addMessage(
                    "Null userId in the provided CommentRatingVo");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUser()) ==
                null) {
            multipleMessageException.addMessage(
                    "No such user associated with CommentRatingVo.userId");
        }
        if (vo.getValue() < -1 || vo.getValue() > 1) {
            multipleMessageException.addMessage(
                    "int Value in the provided CommentRatingVo " +
                    "must be less than or equal to 1 and greater than or equal to -1");
        }

        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    /**
     * Returns a CommentRatingEntity using the information in the provided
     * CommentRatingVo.
     *
     * @param em EntityManager
     * @param vo CommentRatingVo
     * @return CommentRatingEntity
     * @throws InvalidVoException
     */
    @Override
    public CommentRatingEntity voToEntity(EntityManager em, CommentRatingVo vo)
            throws MultipleMessagesException, DataBaseException {

        validateVo(em, vo);

        CommentRatingEntity commentRatingEntity = new CommentRatingEntity();
        commentRatingEntity.setId(vo.getId());
        commentRatingEntity.setValue(vo.getValue());

        commentRatingEntity.setUser(this.getDaoFactory().getUserDao().getById(em,
                vo.getUser()));
        commentRatingEntity.setComment(this.getDaoFactory().getCommentDao().
                getById(em, vo.getEvaluetedObjectId()));

        return commentRatingEntity;
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
    public CommentRatingVo getByCommentIdAndUserId(EntityManager em,
            long commentId, long userId) throws DataBaseException {
        CommentRatingEntity commentRatingEntity = this.getDaoFactory().
                getCommentRatingDao().getByCommentIdAndUserId(em, commentId,
                userId);
        if (commentRatingEntity == null) {
            return null;
        }
        return commentRatingEntity.toVo();
    }

    /**
     * Returns a summary of the ratings associated with a specified comment.
     *
     * @param em the entity manager
     * @param commentId
     * @return a RatingSummaryVo object with the information associated with the
     * ratings corresponding to the specified comment
     */
    public RatingSummaryVo getSummary(EntityManager em, long commentId) throws
            DataBaseException {
        return getDaoFactory().getCommentRatingDao().getSummary(em, commentId);
    }
}
