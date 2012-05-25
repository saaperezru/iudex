package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
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
    public CourseRatingsService(AbstractDaoBuilder daoFactory) {
        super(daoFactory,
                new SimpleCreate<CourseRatingEntity>(daoFactory.
                getCourseRatingDao()),
                new SimpleRead<CourseRatingEntity>(
                daoFactory.getCourseRatingDao()),
                new SimpleUpdate<CourseRatingEntity>(daoFactory.
                getCourseRatingDao()),
                new SimpleRemove<CourseRatingEntity>(daoFactory.
                getCourseRatingDao()));
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
    public void validateVoForCreation(EntityManager em, CourseRatingVo vo) throws
            DataBaseException, MultipleMessagesException {
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.getMessages().add("Null CourseRatingVo");
            throw multipleMessageException;
        }
        if (vo.getCourseId() == null) {
            multipleMessageException.getMessages().add(
                    "Null courseId in the provided CourseRatingVo");
        } else if (getDaoFactory().getCourseDao().getById(em, vo.getCourseId()) ==
                null) {
            multipleMessageException.getMessages().add(
                    "No such course  associated with CourseRatingVo.courseId");
        }
        if (vo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "Null userId in the provided CourseRatingVo");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) ==
                null) {
            multipleMessageException.addMessage(
                    "No such user associated with CourseRatingVo.userId");
        }
        if (vo.getValue() < 0.0 || vo.getValue() > 5.0) {
            multipleMessageException.addMessage(
                    "int Value in the provided CourseRatingVo " +
                    
                    "must be less than or equal to 5.0 and greater than or equal to 0.0");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, CourseRatingVo valueObject) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.getMessages().add("courseRating.id.null");
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

        CourseRatingEntity courseRatingEntity = new CourseRatingEntity();
        courseRatingEntity.setId(vo.getId());
        courseRatingEntity.setValue(vo.getValue());

        courseRatingEntity.setCourse(getDaoFactory().getCourseDao().getById(em,
                vo.getCourseId()));

        courseRatingEntity.setUser(getDaoFactory().getUserDao().getById(em, vo.
                getUserId()));

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
