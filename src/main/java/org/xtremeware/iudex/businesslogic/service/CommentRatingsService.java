package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.BinaryRatingVo;

/**
 *
 * @author josebermeo
 */
public class CommentRatingsService extends BinaryRatingService<CommentRatingEntity> {

    /**
     * CommentRatingsService constructor
     *
     * @param daoFactory
     */
    public CommentRatingsService(AbstractDaoBuilder daoFactory, Create create,Read read, Update update,Delete delete) {
        super(daoFactory,create,read,update,delete,daoFactory.getCommentRatingDao());
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
            throws MultipleMessagesException, DataBaseException {

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
        } else if (getDaoFactory().getCommentDao().read(entityManager, binaryRatingVo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "commentRating.commentId.element.notFound");
        }
        if (binaryRatingVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "commentRating.userId.null");
        } else if (getDaoFactory().getUserDao().read(entityManager, binaryRatingVo.getUserId())
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
            throws MultipleMessagesException, DataBaseException {

        CommentRatingEntity commentRatingEntity = new CommentRatingEntity();
        commentRatingEntity.setId(binaryRatingVo.getId());
        commentRatingEntity.setValue(binaryRatingVo.getValue());

        commentRatingEntity.setUser(this.getDaoFactory().getUserDao().
                read(entityManager,binaryRatingVo.getUserId()));
        commentRatingEntity.setComment(this.getDaoFactory().getCommentDao().
                read(entityManager, binaryRatingVo.getEvaluatedObjectId()));

        return commentRatingEntity;
    }
	@Override
	protected void validateVoForUpdate(EntityManager entityManager, BinaryRatingVo valueObject) throws MultipleMessagesException, DataBaseException {
		validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("commentRating.id.null");
            throw multipleMessageException;
        }
	}
}
