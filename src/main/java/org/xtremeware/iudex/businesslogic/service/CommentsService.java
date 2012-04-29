/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CommentDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * Supports operations of queries about Comments submitted to the system
 *
 * @author juan
 */
public class CommentsService extends CrudService<CommentVo> {

    public final int MAX_COMMENT_LENGTH;
    public final int MAX_COMMENTS_PER_DAY;

    /**
     * Constructor
     *
     * @param daoFactory a daoFactory
     */
    public CommentsService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
        super(daoFactory);
        MAX_COMMENTS_PER_DAY = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENTS_PER_DAY));
        MAX_COMMENT_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENT_LENGTH));

    }

    /**
     * Returns a list with all the coments associated to a course
     *
     * @param em the entity manager
     * @param courseId the Id of the course
     * @return comments in the specified course
     */
    public List<CommentVo> getByCourseId(DataAccessAdapter em, long courseId) throws DataAccessException {
        return ((CommentDao) getDao()).getByCourseId(em, courseId);
    }

    /**
     * Returns the JpaCommentDao from DaoFactory
     *
     * @return JpaCommentDao
     */
    protected CrudDao<CommentVo, CommentEntity> getDao() {
        return getDaoFactory().getCommentDao();
    }

    /**
     * Validates wheter the CommentVo object satisfies the business rules and
     * contains correct references to other objects
     *
     * @param em the entity manager
     * @param vo the CommentVo
     * @throws InvalidVoException in case the business rules are violated
     */
    public void validateVo(DataAccessAdapter em, CommentVo vo) throws InvalidVoException, DataAccessException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null CommentVo");
        }
        if (vo.getContent() == null) {
            throw new InvalidVoException("String Content in the provided CommentVo cannot be null");
        } else if (vo.getCourseId() == null) {
            throw new InvalidVoException("Long CourseId in the provided CommentVo cannot be null");
        } else if (vo.getDate() == null) {
            throw new InvalidVoException("Date Date in the provided CommentVo cannot be null");
        } else if (vo.getRating() == null) {
            throw new InvalidVoException("Float Rating in the provided CommentVo cannot be null");
        } else if (vo.getUserId() == null) {
            throw new InvalidVoException("Long UserId in the provided CommentVo cannot be null");
        } else if (vo.getRating() < 0.0F || vo.getRating() > 5.0F) {
            throw new InvalidVoException("Float Rating in the provided CommentVo must be greater or equal than 0.0 and less or equal than 5.0");
        }

        vo.setContent(vo.getContent().trim().replaceAll(" +", " "));

        try {
            if (vo.getContent().length() < 1 || vo.getContent().length() > MAX_COMMENT_LENGTH) {
                throw new InvalidVoException("String Content length in the provided CommentVo must be grater or equal than 1 and less or equal than " + MAX_COMMENT_LENGTH);
            }
        } catch (NumberFormatException e) {
        }
        if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId()) == null) {
            throw new InvalidVoException("Long CourseID in the provided CommentVo does not have matches with existent courses");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) == null) {
            throw new InvalidVoException("Long UserId in the provided CommentVo does not have matches with existent users");
        }
    }

    public CommentVo create(DataAccessAdapter em, CommentVo vo) throws InvalidVoException, MaxCommentsLimitReachedException, DataAccessException {
        validateVo(em, vo);

        if (checkUserCommentsCounter(em, vo.getUserId()) >= MAX_COMMENTS_PER_DAY) {
            throw new MaxCommentsLimitReachedException("Maximum comments per day reached");
        }

        return getDao().persist(em, vo);
    }

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param em entity manager
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    public int checkUserCommentsCounter(DataAccessAdapter em, long userId) throws DataAccessException {
        return ((CommentDao) getDao()).getUserCommentsCounter(em, userId);
    }

    public CommentVo update(DataAccessAdapter em, CommentVo vo) throws InvalidVoException, ExternalServiceConnectionException, DataAccessException {
        validateVo(em, vo);
        return getDao().merge(em, vo);

    }

    /**
     * Remove the comment and all the CommentRatings associated to him
     *
     * @param em entity manager
     * @param id id of the comment
     */
    public void remove(DataAccessAdapter em, long id) throws DataAccessException {
        List<CommentRatingEntity> ratings = getDaoFactory().getCommentRatingDao().getByCommentId(em, id);

        for (CommentRatingEntity rating : ratings) {
            getDaoFactory().getCommentRatingDao().remove(em, rating.getId());
        }
        getDao().remove(em, id);
    }

    public CommentVo getById(DataAccessAdapter em, long id) throws DataAccessException {
        return getDao().getById(em, id);
    }
}
