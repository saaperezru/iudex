package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;

public class SubjectRatingsService extends BinaryRatingService<SubjectRatingEntity> {

    public SubjectRatingsService(AbstractDaoBuilder daoFactory, Read read, Delete delete) {
        super(daoFactory,read,delete,daoFactory.getSubjectRatingDao());
    }

    @Override
    public void validateVoForCreation(EntityManager entityManager,
            BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (binaryRatingVo == null) {
            multipleMessageException.addMessage("subjectRating.null");
            throw multipleMessageException;
        }
        if (binaryRatingVo.getEvaluatedObjectId() == null) {
            multipleMessageException.addMessage("subjectRating.subjectId.null");
        } else if (getDaoFactory().getSubjectDao().read(entityManager, binaryRatingVo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage("subjectRating.subjectId.element.notFound");
        }
        if (binaryRatingVo.getUserId() == null) {
            multipleMessageException.addMessage("subjectRating.userId.null");
        } else if (getDaoFactory().getUserDao().read(entityManager, binaryRatingVo.getUserId()) == null) {
            multipleMessageException.addMessage("subjectRating.userId.element.notFound");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
        if (binaryRatingVo.getValue() > 1 || binaryRatingVo.getValue() < -1) {
            multipleMessageException.addMessage("subjectRating.value.invalidRating");
        }
    }

    @Override
    public SubjectRatingEntity voToEntity(EntityManager entityManager,
            BinaryRatingVo binaryRatingVo) throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        SubjectRatingEntity entity = new SubjectRatingEntity();
        entity.setId(binaryRatingVo.getId());
        entity.setSubject(getDaoFactory().getSubjectDao().read(entityManager,
                binaryRatingVo.getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().
                read(entityManager, binaryRatingVo.getUserId()));
        entity.setValue(binaryRatingVo.getValue());
        return entity;
    }
}
