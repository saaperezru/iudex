package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ProfessorRatingVo;

public class ProfessorRatingsService extends RatingService<ProfessorRatingVo, ProfessorRatingEntity> {

    public ProfessorRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleRead<ProfessorRatingEntity>(daoFactory.getProfessorRatingDao()),
                new SimpleRemove<ProfessorRatingEntity>(daoFactory.getProfessorRatingDao()),
                daoFactory.getProfessorRatingDao());
    }

    @Override
    public void validateVo(EntityManager em, ProfessorRatingVo vo)
            throws MultipleMessagesException, DataBaseException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage(
                    "professorRating.null");
            throw multipleMessageException;
        }
        if (vo.getValue() > 1 || vo.getValue() < -1) {
            multipleMessageException.addMessage(
                    "professorRating.value.invalidValue");
        }
        if (vo.getEvaluatedObjectId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.null");
        } else if (getDaoFactory().getProfessorDao().getById(em, vo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.element.notFound");
        }
        if (vo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.null");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId())
                == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.element.notFound");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }

    }

    @Override
    public ProfessorRatingEntity voToEntity(EntityManager em,
            ProfessorRatingVo vo)
            throws MultipleMessagesException, DataBaseException {
        validateVo(em, vo);
        ProfessorRatingEntity entity = new ProfessorRatingEntity();
        entity.setId(vo.getId());
        entity.setProfessor(getDaoFactory().getProfessorDao().getById(em, vo.getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));
        entity.setValue(vo.getValue());
        return entity;
    }
}
