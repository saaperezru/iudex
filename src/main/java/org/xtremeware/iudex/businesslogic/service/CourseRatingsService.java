package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 *
 * @author josebermeo
 */
public class CourseRatingsService extends CrudService<CourseRatingVo, CourseRatingEntity> {

	/**
	 * CourseRatingsService constructor
	 *
	 * @param daoFactory
	 */
	public CourseRatingsService(AbstractDaoFactory daoFactory) {
		super(daoFactory,
				new SimpleCreate<CourseRatingEntity>(daoFactory.getCourseRatingDao()),
				new SimpleRead<CourseRatingEntity>(
				daoFactory.getCourseRatingDao()),
				new SimpleUpdate<CourseRatingEntity>(daoFactory.getCourseRatingDao()),
				new SimpleRemove<CourseRatingEntity>(daoFactory.getCourseRatingDao()));
	}

	/**
	 * Validate the provided CourseRatingVo, if the CourseRatingVo is not
	 * correct the methods throws an exception
	 *
	 * @param em EntityManager
	 * @param vo CourseRatingVo to validate
	 * @throws InvalidVoException
	 */
	@Override
	public void validateVo(EntityManager em, CourseRatingVo vo) throws
			DataBaseException, MultipleMessagesException {
		MultipleMessagesException multipleMessageException =
				new MultipleMessagesException();
		if (vo == null) {
			multipleMessageException.getMessages().add("courseRating.null");
			throw multipleMessageException;
		}
		if (vo.getCourseId() == null) {
			multipleMessageException.getMessages().add(
					"courseRating.courseId.null");
		} else if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId())
				== null) {
			multipleMessageException.getMessages().add(
					"courseRating.courseId.notFound");
		}
		if (vo.getUserId() == null) {
			multipleMessageException.addMessage(
					"courseRating.userId.null");
		} else if (getDaoFactory().getUserDao().getById(em, vo.getUserId())
				== null) {
			multipleMessageException.addMessage(
					"courseRating.userId.notFound");
		}
		if (vo.getValue() < 0.0 || vo.getValue() > 5.0) {
			multipleMessageException.addMessage(
					"courseRating.value.invalidRange");
		}
		if (!multipleMessageException.getMessages().isEmpty()) {
			throw multipleMessageException;
		}
	}

	/**
	 * Returns a CourseRatingEntity using the information in the provided
	 * CourseRatingVo.
	 *
	 * @param em EntityManager
	 * @param vo CourseRatingVo to be transformed
	 * @return CourseRatingEntity
	 * @throws InvalidVoException
	 */
	@Override
	public CourseRatingEntity voToEntity(EntityManager em, CourseRatingVo vo)
			throws DataBaseException, MultipleMessagesException {

		validateVo(em, vo);

		CourseRatingEntity courseRatingEntity = new CourseRatingEntity();
		courseRatingEntity.setId(vo.getId());
		courseRatingEntity.setValue(vo.getValue());

		courseRatingEntity.setCourse(getDaoFactory().getCourseDao().getById(em,
				vo.getCourseId()));

		courseRatingEntity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));

		return courseRatingEntity;
	}

	/**
	 * Returns a CourseRatingVo associated with the provided userId and courseId
	 *
	 * @param em EntityManager
	 * @param courseId course identifier
	 * @param userId user identifier
	 * @return CourseRatingVo
	 */
	public CourseRatingVo getByCourseIdAndUserId(EntityManager em, long courseId,
			long userId)
			throws DataBaseException {
		CourseRatingEntity courseRatingEntity = getDaoFactory().
				getCourseRatingDao().
				getByCourseIdAndUserId(em, courseId, userId);
		if (courseRatingEntity == null) {
			return null;
		}
		return courseRatingEntity.toVo();
	}
}
