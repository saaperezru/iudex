package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
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

    /**
     * CourseRatingsService constructor
     *
     * @param daoFactory
     */
    public CourseRatingsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Remove remove) {

        super(daoFactory, create, read, update, remove);
    }

    /**
     * Validate the provided CourseRatingVo, if the CourseRatingVo is not
     * correct the methods throws an exception
     *
     * @param entityManager EntityManager
     * @param courseRatingVo CourseRatingVo to validate
     * @throws InvalidVoException
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, CourseRatingVo courseRatingVo) throws
            DataBaseException, MultipleMessagesException {
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (courseRatingVo == null) {
            multipleMessageException.getMessages().add("Null CourseRatingVo");
            throw multipleMessageException;
        }
        if (courseRatingVo.getCourseId() == null) {
            multipleMessageException.getMessages().add(
                    "Null courseId in the provided CourseRatingVo");
        } else if (getDaoFactory().getCourseDao().getById(entityManager, courseRatingVo.getCourseId()) ==
                null) {
            multipleMessageException.getMessages().add(
                    "No such course  associated with CourseRatingVo.courseId");
        }
        if (courseRatingVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "Null userId in the provided CourseRatingVo");
        } else if (getDaoFactory().getUserDao().getById(entityManager, courseRatingVo.getUserId()) ==
                null) {
            multipleMessageException.addMessage(
                    "No such user associated with CourseRatingVo.userId");
        }
        if (courseRatingVo.getValue() < 0.0 || courseRatingVo.getValue() > 5.0) {
            multipleMessageException.addMessage(
                    "int Value in the provided CourseRatingVo " +
                    
                    "must be less than or equal to 5.0 and greater than or equal to 0.0");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, CourseRatingVo courseRatingVo) 
            throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
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

        courseRatingEntity.setCourse(getDaoFactory().getCourseDao().getById(entityManager,
                courseRatingVo.getCourseId()));

        courseRatingEntity.setUser(getDaoFactory().getUserDao().getById(entityManager, courseRatingVo.
                getUserId()));

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
