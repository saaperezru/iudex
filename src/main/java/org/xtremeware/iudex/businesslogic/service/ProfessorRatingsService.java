package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Read;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;

public class ProfessorRatingsService extends BinaryRatingService< ProfessorRatingEntity> {

    public ProfessorRatingsService(AbstractDaoBuilder daoFactory,Create create, Read read, Update update,Delete delete) {
        super(daoFactory,create,read,update,delete,daoFactory.getProfessorRatingDao());
    }

    @Override
    public void validateVoForCreation(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException, DataBaseException {

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
        } else if (getDaoFactory().getProfessorDao().read(entityManager, binaryRatingVo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.element.notFound");
        }
        if (binaryRatingVo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.null");
        } else if (getDaoFactory().getUserDao().read(entityManager, binaryRatingVo.getUserId())
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
            throws MultipleMessagesException, DataBaseException {

        ProfessorRatingEntity entity = new ProfessorRatingEntity();
        entity.setId(binaryRatingVo.getId());
        entity.setProfessor(getDaoFactory().getProfessorDao().
                read(entityManager, binaryRatingVo.getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().
                read(entityManager, binaryRatingVo.getUserId()));
        entity.setValue(binaryRatingVo.getValue());
        return entity;
    }
	@Override
	protected void validateVoForUpdate(EntityManager entityManager, BinaryRatingVo valueObject) throws MultipleMessagesException, DataBaseException {
		validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("professorRating.id.null");
            throw multipleMessageException;
        }
	}
}
