package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingsService extends RatingService<CommentRatingEntity> {

    /**
     * CommentRatingsService constructor
     *
     * @param daoFactory
     */
    public CommentRatingsService(AbstractDaoBuilder daoFactory) {
        super(daoFactory,
                new SimpleRead<CommentRatingEntity>(daoFactory.getCommentRatingDao()),
                new SimpleRemove<CommentRatingEntity>(daoFactory.getCommentRatingDao()),
                daoFactory.getCommentRatingDao());
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
    public void validateVoForCreation(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();

        if (binaryRatingVo == null) {
            multipleMessageException.addMessage(
                    "commentRating.null");
            throw multipleMessageException;
        }
        if (binaryRatingVo.getEvaluatedObjectId() == null) {
            multipleMessageException.addMessage(
                    "commentRating.commentId.null");
        } else if (getDaoFactory().getCommentDao().getById(entityManager, binaryRatingVo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "commentRating.commentId.element.notFound");
        }
        if (binaryRatingVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "commentRating.userId.null");
        } else if (getDaoFactory().getUserDao().getById(entityManager, binaryRatingVo.getUserId())
                == null) {
            multipleMessageException.addMessage(
                    "commentRating.userId.element.notFound");
        }
        if (binaryRatingVo.getValue() < -1 || binaryRatingVo.getValue() > 1) {
            multipleMessageException.addMessage(
                    "commentRating.value.invalidValue");
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
    public CommentRatingEntity voToEntity(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        CommentRatingEntity commentRatingEntity = new CommentRatingEntity();
        commentRatingEntity.setId(binaryRatingVo.getId());
        commentRatingEntity.setValue(binaryRatingVo.getValue());

        commentRatingEntity.setUser(this.getDaoFactory().getUserDao().
                getById(entityManager,binaryRatingVo.getUserId()));
        commentRatingEntity.setComment(this.getDaoFactory().getCommentDao().
                getById(entityManager, binaryRatingVo.getEvaluatedObjectId()));

        return commentRatingEntity;
    }
}
