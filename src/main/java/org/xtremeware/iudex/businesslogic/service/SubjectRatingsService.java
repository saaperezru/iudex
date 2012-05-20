package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.SubjectRatingVo;

public class SubjectRatingsService extends RatingService<SubjectRatingVo, SubjectRatingEntity> {

    public SubjectRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleRead<SubjectRatingEntity>(daoFactory.getSubjectRatingDao()),
                new SimpleRemove<SubjectRatingEntity>(daoFactory.getSubjectRatingDao()),
                daoFactory.getSubjectRatingDao());
    }

    @Override
    public void validateVo(EntityManager em, SubjectRatingVo vo) throws
            MultipleMessagesException, DataBaseException {
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("subjectRating.null");
            throw multipleMessageException;
        }
        if (vo.getValue() > 1 || vo.getValue() < -1) {
            multipleMessageException.addMessage("subjectRating.value.invalidRating");
        }
        if (vo.getEvaluatedObjectId() == null) {
            multipleMessageException.addMessage("subjectRating.subjectId.null");
        } else if (getDaoFactory().getSubjectDao().getById(em, vo.getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage("subjectRating.subjectId.element.notFound");
        }
        if (vo.getUserId() == null) {
            multipleMessageException.addMessage("subjectRating.userId.null");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) == null) {
            multipleMessageException.addMessage("subjectRating.userId.element.notFound");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public SubjectRatingEntity voToEntity(EntityManager em, SubjectRatingVo vo)
            throws MultipleMessagesException, DataBaseException {
        validateVo(em, vo);
        SubjectRatingEntity entity = new SubjectRatingEntity();
        entity.setId(vo.getId());
        entity.setSubject(getDaoFactory().getSubjectDao().getById(em,
                vo.getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));
        entity.setValue(vo.getValue());
        return entity;
    }
}
