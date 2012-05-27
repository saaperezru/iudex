package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * Supports operations of queries about Comments submitted to the system
 *
 * @author juan
 */
public class CommentsService extends CrudService<CommentVo, CommentEntity> {

    public final int MAX_COMMENT_LENGTH;

    /**
     * Constructor
     *
     * @param daoFactory a daoFactory
     */
    public CommentsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Remove remove) {

        super(daoFactory, create, read, update, remove);
        MAX_COMMENT_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENT_LENGTH));

    }

    /**
     * Returns a list with all the comments associated to a course
     *
     * @param entityManager the entity manager
     * @param courseId the Id of the course
     * @return comments in the specified course
     */
    public List<CommentVo> getByCourseId(EntityManager entityManager, long courseId)
            throws DataBaseException {
        List<CommentEntity> commentEntitys = getDaoFactory().getCommentDao().getByCourseId(entityManager, courseId);

        List<CommentVo> commentVos = new ArrayList<CommentVo>();

        for (CommentEntity commentEntity : commentEntitys) {
            commentVos.add(commentEntity.toVo());
        }

        return commentVos;
    }

    /**
     * Validates whether the CommentVo object satisfies the business rules and
     * contains correct references to other objects
     *
     * @param entityManager the entity manager
     * @param commentVo the CommentVo
     * @throws InvalidVoException in case the business rules are violated
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, CommentVo commentVo)
            throws MultipleMessagesException, DataBaseException {

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();

        if (commentVo == null) {
            multipleMessageException.addMessage(
                    "comment.null");
            throw multipleMessageException;
        }

        if (commentVo.getContent() == null) {
            multipleMessageException.addMessage(
                    "comment.content.null");
        } else {
            commentVo.setContent(SecurityHelper.sanitizeHTML(commentVo.getContent()));
            if (commentVo.getContent().length() < 1 || commentVo.getContent().length() > MAX_COMMENT_LENGTH) {
                multipleMessageException.addMessage(
                        "comment.content.invalidSize");
            }
        }

        if (commentVo.getCourseId() == null) {
            multipleMessageException.addMessage(
                    "comment.courseId.null");
        } else if (getDaoFactory().getCourseDao().getById(entityManager, commentVo.getCourseId()) == null) {
            multipleMessageException.addMessage(
                    "comment.courseId.element.notFound");
        }

        if (commentVo.getDate() == null) {
            multipleMessageException.addMessage(
                    "comment.date.null");
        }

        if (commentVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "comment.userId.null");
        } else if (getDaoFactory().getUserDao().getById(entityManager, commentVo.getUserId()) == null) {
            multipleMessageException.addMessage(
                    "comment.userId.element.notFound");
        }

        if (commentVo.getRating() == null) {
            multipleMessageException.addMessage(
                    "comment.rating.null");
        } else if (commentVo.getRating() < 0.0F || commentVo.getRating() > 5.0F) {
            multipleMessageException.addMessage(
                    "comment.rating.invalidRating");
        }

        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    /**
     * Creates a Entity with the data of the value object
     *
     * @param entityManager the entity manager
     * @param commentVo the CommentVo
     * @return an Entity with the Comment value object data
     * @throws InvalidVoException
     */
    @Override
    public CommentEntity voToEntity(EntityManager entityManager, CommentVo commentVo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {

        CommentEntity entity = new CommentEntity();

        entity.setAnonymous(commentVo.isAnonymous());
        entity.setContent(commentVo.getContent());
        entity.setDate(commentVo.getDate());
        entity.setId(commentVo.getId());
        entity.setRating(commentVo.getRating());

        entity.setCourse(getDaoFactory().getCourseDao().getById(entityManager, commentVo.getCourseId()));
        entity.setUser(getDaoFactory().getUserDao().getById(entityManager, commentVo.getUserId()));

        return entity;
    }

    @Override
    protected void validateVoForUpdate(EntityManager entityManager, CommentVo valueObject) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("comment.id.null");
            throw multipleMessageException;
        }
    }
}
