package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
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

    private final int MAX_COMMENT_LENGTH;
    private static final float MAX_RATE = 5.0F;
    private static final float MIN_RATE = 0.0F;

    /**
     * Constructor
     *
     * @param daoFactory a daoFactory
     */
    public CommentsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {

        super(daoFactory, create, read, update, delete);
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

        MultipleMessagesException multipleMessagesException = new MultipleMessagesException();

        if (commentVo == null) {
            multipleMessagesException.addMessage(
                    "comment.null");
            throw multipleMessagesException;
        }

        checkContent(multipleMessagesException, commentVo.getContent());
        checkCourseId(multipleMessagesException, commentVo.getCourseId(), entityManager);

        if (commentVo.getDate() == null) {
            multipleMessagesException.addMessage(
                    "comment.date.null");
        }
        
        checkUserId(multipleMessagesException, commentVo.getUserId(), entityManager);
        checkRating(multipleMessagesException, commentVo.getRating());

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }
        commentVo.setContent(SecurityHelper.sanitizeHTML(commentVo.getContent()));
    }

    private void checkContent(MultipleMessagesException multipleMessagesException,
            String content) {
        if (content == null) {
            multipleMessagesException.addMessage(
                    "comment.content.null");
        } else {
            if (content.isEmpty() || content.length() > MAX_COMMENT_LENGTH) {
                multipleMessagesException.addMessage(
                        "comment.content.invalidSize");
            }
        }
    }
    
    private void checkCourseId(MultipleMessagesException multipleMessagesException,
            Long courseid, EntityManager entityManager) throws DataBaseException {
        if (courseid == null) {
            multipleMessagesException.addMessage(
                    "comment.courseId.null");
        } else if (getDaoFactory().getCourseDao().read(entityManager, courseid) == null) {
            multipleMessagesException.addMessage(
                    "comment.courseId.element.notFound");
        }
    }
    
    private void checkUserId(MultipleMessagesException multipleMessagesException,
            Long userid, EntityManager entityManager) throws DataBaseException {
        if (userid == null) {
            multipleMessagesException.addMessage(
                    "comment.userId.null");
        } else if (getDaoFactory().getUserDao().read(entityManager, userid) == null) {
            multipleMessagesException.addMessage(
                    "comment.userId.element.notFound");
        }
    }
    
    private void checkRating(MultipleMessagesException multipleMessagesException,
            Float rating) {
        if (rating == null) {
            multipleMessagesException.addMessage(
                    "comment.rating.null");
        } else if (rating < MIN_RATE || rating > MAX_RATE) {
            multipleMessagesException.addMessage(
                    "comment.rating.invalidRating");
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
            throws MultipleMessagesException, DataBaseException {

        CommentEntity entity = new CommentEntity();

        entity.setAnonymous(commentVo.isAnonymous());
        entity.setContent(commentVo.getContent());
        entity.setDate(commentVo.getDate());
        entity.setId(commentVo.getId());
        entity.setRating(commentVo.getRating());

        entity.setCourse(getDaoFactory().getCourseDao().read(entityManager, commentVo.getCourseId()));
        entity.setUser(getDaoFactory().getUserDao().read(entityManager, commentVo.getUserId()));
        return entity;
    }

    @Override
    protected void validateVoForUpdate(EntityManager entityManager, CommentVo valueObject)
            throws MultipleMessagesException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("comment.id.null");
            throw multipleMessageException;
        }
    }
}
