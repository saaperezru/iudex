package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;

public class ProfessorRatingsService extends RatingService< ProfessorRatingEntity> {

    public ProfessorRatingsService(AbstractDaoBuilder daoFactory) {
        super(daoFactory,
                new SimpleRead<ProfessorRatingEntity>(daoFactory.getProfessorRatingDao()),
                new SimpleRemove<ProfessorRatingEntity>(daoFactory.getProfessorRatingDao()),
                daoFactory.getProfessorRatingDao());
    }

    @Override
    public void validateVoForCreation(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (binaryRatingVo == null) {
            multipleMessageException.addMessage(
                    "professorRating.null");
            throw multipleMessageException;
        }
        if (binaryRatingVo.getEvaluatedObjectId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.null");
        } else if (getDaoFactory().getProfessorDao().getById(entityManager, binaryRatingVo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.element.notFound");
        }
        if (binaryRatingVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.null");
        } else if (getDaoFactory().getUserDao().getById(entityManager, binaryRatingVo.getUserId())
                == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.element.notFound");
        }
        if (binaryRatingVo.getValue() > 1 || binaryRatingVo.getValue() < -1) {
            multipleMessageException.addMessage(
                    "professorRating.value.invalidValue");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }

    }

    @Override
    public ProfessorRatingEntity voToEntity(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        ProfessorRatingEntity entity = new ProfessorRatingEntity();
        entity.setId(binaryRatingVo.getId());
        entity.setProfessor(getDaoFactory().getProfessorDao().
                getById(entityManager, binaryRatingVo.getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().
                getById(entityManager, binaryRatingVo.getUserId()));
        entity.setValue(binaryRatingVo.getValue());
        return entity;
    }
}
