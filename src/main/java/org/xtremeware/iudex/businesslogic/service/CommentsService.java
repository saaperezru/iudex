/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CommentDao;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
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
	public List<CommentVo> getByCourseId(EntityManager em, long courseId) {
		List<CommentEntity> entities = ((CommentDao) getDao()).getByCourseId(em, courseId);

		if (entities.isEmpty()) {
			return null;
		}

		List<CommentVo> vos = new ArrayList<CommentVo>();

		for (CommentEntity e : entities) {
			vos.add(e.toVo());
		}

		return vos;
	}

	/**
	 * Returns the CommentDao from DaoFactory
	 *
	 * @return CommentDao
	 */
	protected Dao<CommentEntity> getDao() {
		return getDaoFactory().getCommentDao();
	}

	/**
	 * Validates wheter the CommentVo object satisfies the business rules
	 * and contains correct references to other objects
	 *
	 * @param em the entity manager
	 * @param vo the CommentVo
	 * @throws InvalidVoException in case the business rules are violated
	 */
	public void validateVo(EntityManager em, CommentVo vo) throws InvalidVoException {
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

	/**
	 * Creates a Entity with the data of the value object
	 *
	 * @param em the entity manager
	 * @param vo the CommentVo
	 * @return an Entity with the Comment value object data
	 * @throws InvalidVoException
	 */
	public CommentEntity voToEntity(EntityManager em, CommentVo vo) throws InvalidVoException {

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

	public CommentVo create(EntityManager em, CommentVo vo) throws InvalidVoException, MaxCommentsLimitReachedException {
		validateVo(em, vo);

		if (checkUserCommentsCounter(em, vo.getUserId()) >= MAX_COMMENTS_PER_DAY) {
			throw new MaxCommentsLimitReachedException("Maximum comments per day reached");
		}

		return getDao().persist(em, voToEntity(em, vo)).toVo();
	}

	/**
	 * Returns the number of comments submitted by a user on the current
	 * date
	 *
	 * @param em entity manager
	 * @param userId id of the user
	 * @return number of comments submitted on the current day
	 */
	public int checkUserCommentsCounter(EntityManager em, Long userId) {
		return ((CommentDao) getDao()).getUserCommentsCounter(em, userId);
	}

	public CommentVo update(EntityManager em, CommentVo vo) throws InvalidVoException, ExternalServiceConnectionException {
		validateVo(em, vo);
		return getDao().merge(em, voToEntity(em, vo)).toVo();

	}

        /**
         * Remove the comment and all the CommentRatings associated  to him
         * 
         * @param em entity manager
         * @param id id of the comment
         */
	public void remove(EntityManager em, long id) {
             List<CommentRatingEntity> ratings = getDaoFactory().getCommentRatingDao().getByCommentId(em, id);
                
             for (CommentRatingEntity rating : ratings){
                  getDaoFactory().getCommentRatingDao().remove(em,rating.getId());
             }
             getDao().remove(em, id);
	}

	public CommentVo getById(EntityManager em, long id) {
                return getDao().getById(em, id).toVo();
	}
}
