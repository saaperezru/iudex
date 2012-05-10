package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * Supports operations of queries about Comments submitted to the system
 *
 * @author juan
 */
public class CommentsService {

    public final int MAX_COMMENT_LENGTH;
    public final int MAX_COMMENTS_PER_DAY;
    private AbstractDaoFactory daoFactory;

    /**
     * Constructor
     *
     * @param daoFactory a daoFactory
     */
    public CommentsService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
        this.daoFactory = daoFactory;
        MAX_COMMENTS_PER_DAY = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENTS_PER_DAY));
        MAX_COMMENT_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENT_LENGTH));

    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }

    /**
     * Returns a list with all the comments associated to a course
     *
     * @param em the entity manager
     * @param courseId the Id of the course
     * @return comments in the specified course
     */
    public List<CommentVo> getByCourseId(EntityManager em, long courseId)
            throws DataBaseException {
        List<CommentEntity> entities = getDaoFactory().getCommentDao().getByCourseId(em, courseId);
        
        List<CommentVo> vos = new ArrayList<CommentVo>();

        if (entities.isEmpty()) {
            return vos;
        }

        for (CommentEntity e : entities) {
            vos.add(e.toVo());
        }

        return vos;
    }

    /**
     * Validates whether the CommentVo object satisfies the business rules and
     * contains correct references to other objects
     *
     * @param em the entity manager
     * @param vo the CommentVo
     * @throws InvalidVoException in case the business rules are violated
     */
    public void validateVo(EntityManager em, CommentVo vo) throws
             MultipleMessagesException, DataBaseException {

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();

        if (vo == null) {
            multipleMessageException.addMessage(
                    "comment.null");
            throw multipleMessageException;
        }

        if (vo.getContent() == null) {
            multipleMessageException.addMessage(
                    "comment.content.null");
        } else {
            vo.setContent(SecurityHelper.sanitizeHTML(vo.getContent()));
            if (vo.getContent().length() < 1 || vo.getContent().length() > MAX_COMMENT_LENGTH) {
                multipleMessageException.addMessage(
                        "comment.content.invalidSize");
            }
        }

        if (vo.getCourseId() == null) {
            multipleMessageException.addMessage(
                    "comment.courseId.null");
        } else if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId()) == null) {
            multipleMessageException.addMessage(
                    "comment.courseId.element.notFound");
        }

        if (vo.getDate() == null) {
            multipleMessageException.addMessage(
                    "comment.date.null");
        }

        if (vo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "comment.userId.null");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) == null) {
            multipleMessageException.addMessage(
                    "comment.userId.element.notFound");
        }

        if (vo.getRating() == null) {
            multipleMessageException.addMessage(
                    "comment.rating.null");
        } else if (vo.getRating() < 0.0F || vo.getRating() > 5.0F) {
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
     * @param em the entity manager
     * @param vo the CommentVo
     * @return an Entity with the Comment value object data
     * @throws InvalidVoException
     */
    public CommentEntity voToEntity(EntityManager em, CommentVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {

        validateVo(em, vo);

        CommentEntity entity = new CommentEntity();

        entity.setAnonymous(vo.isAnonymous());
        entity.setContent(vo.getContent());
        entity.setDate(vo.getDate());
        entity.setId(vo.getId());
        entity.setRating(vo.getRating());

        entity.setCourse(getDaoFactory().getCourseDao().getById(em, vo.getCourseId()));
        entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));

        return entity;
    }

    public CommentVo create(EntityManager em, CommentVo vo)
            throws MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException, MaxCommentsLimitReachedException {
        validateVo(em, vo);

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (checkUserCommentsCounter(em, vo.getUserId()) >= MAX_COMMENTS_PER_DAY) {
            throw new MaxCommentsLimitReachedException("Maximum comments per day reached");
        }

        return getDaoFactory().getCommentDao().persist(em, voToEntity(em, vo)).toVo();
    }

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param em entity manager
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    public int checkUserCommentsCounter(EntityManager em, Long userId) throws DataBaseException {
        return getDaoFactory().getCommentDao().getUserCommentsCounter(em, userId);
    }

    public CommentVo update(EntityManager em, CommentVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException, DataBaseException {
        validateVo(em, vo);
        return getDaoFactory().getCommentDao().merge(em, voToEntity(em, vo)).toVo();

    }

    /**
     * Remove the comment and all the CommentRatings associated to him
     *
     * @param em entity manager
     * @param id id of the comment
     */
    public void remove(EntityManager em, long id) throws DataBaseException {
        List<CommentRatingEntity> ratings = getDaoFactory().getCommentRatingDao().getByCommentId(em, id);

        for (CommentRatingEntity rating : ratings) {
            getDaoFactory().getCommentRatingDao().remove(em, rating.getId());
        }
        getDaoFactory().getCommentDao().remove(em, id);
    }

    public CommentVo getById(EntityManager em, long id)
            throws DataBaseException {
        return getDaoFactory().getCommentDao().getById(em, id).toVo();
    }
}
