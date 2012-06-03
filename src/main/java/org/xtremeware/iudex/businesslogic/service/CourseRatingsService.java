package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 *
 * @author josebermeo
 */
public class CourseRatingsService extends CrudService<CourseRatingVo, CourseRatingEntity> {
    
    private static final float MAX_RATE = 5.0F;
    private static final float MIN_RATE = 0.0F;

    /**
     * CourseRatingsService constructor
     *
     * @param daoFactory
     */
    public CourseRatingsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {

        super(daoFactory, create, read, update, delete);
    }

	/**
	 * Validate the provided CourseRatingVo, if the CourseRatingVo is not
	 * correct the methods throws an exception
	 *
	 * @param entityManager EntityManager
	 * @param valueObject CourseRatingVo to validate
	 * @throws InvalidVoException
	 */
	@Override
	protected void validateVoForCreation(EntityManager entityManager, CourseRatingVo valueObject) 
                throws MultipleMessagesException, DataBaseException {
		MultipleMessagesException multipleMessageException =
				new MultipleMessagesException();
		if (valueObject == null) {
			multipleMessageException.getMessages().add("courseRating.null");
			throw multipleMessageException;
		}
		if (valueObject.getCourseId() == null) {
			multipleMessageException.getMessages().add(
					"courseRating.courseId.null");
		} else if (getDaoFactory().getCourseDao().read(entityManager, valueObject.getCourseId())
				== null) {
			multipleMessageException.getMessages().add(
					"courseRating.courseId.notFound");
		}
		if (valueObject.getUserId() == null) {
			multipleMessageException.addMessage(
					"courseRating.userId.null");
		} else if (getDaoFactory().getUserDao().read(entityManager, valueObject.getUserId())
				== null) {
			multipleMessageException.addMessage(
					"courseRating.userId.notFound");
		}
		if (valueObject.getValue() < MIN_RATE || valueObject.getValue() > MAX_RATE) {
			multipleMessageException.addMessage(
					"courseRating.value.invalidRange");
		}
		if (!multipleMessageException.getMessages().isEmpty()) {
			throw multipleMessageException;
		}
	}
	@Override
    public void validateVoForUpdate(EntityManager entityManager, CourseRatingVo courseRatingVo)
            throws MultipleMessagesException, DataBaseException {

        validateVoForCreation(entityManager, courseRatingVo);

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (courseRatingVo.getId() == null) {
            multipleMessageException.getMessages().add("courseRating.id.null");
            throw multipleMessageException;
        }
    }

    /**
     * Returns a CourseRatingEntity using the information in the provided
     * CourseRatingVo.
     *
     * @param entityManager EntityManager
     * @param courseRatingVo CourseRatingVo to be transformed
     * @return CourseRatingEntity
     * @throws InvalidVoException
     */
    @Override
    public CourseRatingEntity voToEntity(EntityManager entityManager, CourseRatingVo courseRatingVo)
            throws DataBaseException, MultipleMessagesException {

        CourseRatingEntity courseRatingEntity = new CourseRatingEntity();

        courseRatingEntity.setId(courseRatingVo.getId());
        courseRatingEntity.setValue(courseRatingVo.getValue());

        courseRatingEntity.setCourse(getDaoFactory().getCourseDao().read(entityManager,
                courseRatingVo.getCourseId()));

        courseRatingEntity.setUser(getDaoFactory().getUserDao().read(entityManager, courseRatingVo.getUserId()));

		return courseRatingEntity;
	}

    /**
     * Returns a CourseRatingVo associated with the provided userId and courseId
     *
     * @param entityManager EntityManager
     * @param courseId course identifier
     * @param userId user identifier
     * @return CourseRatingVo
     */
    public CourseRatingVo getByCourseIdAndUserId(EntityManager entityManager, long courseId, long userId)
            throws DataBaseException {

        CourseRatingEntity courseRatingEntity = getDaoFactory().
                getCourseRatingDao().
                getByCourseIdAndUserId(entityManager, courseId, userId);
        if (courseRatingEntity == null) {
            return null;
        }
        return courseRatingEntity.toVo();
    }

}
