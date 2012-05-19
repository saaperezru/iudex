package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;

public class SubjectRatingsService extends CrudService<SubjectRatingVo, SubjectRatingEntity> {

    public SubjectRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<SubjectRatingEntity>(daoFactory.
                getSubjectRatingDao()),
                new SimpleRead<SubjectRatingEntity>(daoFactory.
                getSubjectRatingDao()),
                new SimpleUpdate<SubjectRatingEntity>(daoFactory.
                getSubjectRatingDao()),
                new SimpleRemove<SubjectRatingEntity>(daoFactory.
                getSubjectRatingDao()));
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

    /**
     * Returns a list of SubjectRatings value objects corresponding to the
     * specified subject id
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingVo instances associated with the specified
     * subject
     */
    public List<SubjectRatingVo> getBySubjectId(EntityManager em, long subjectId)
            throws DataBaseException {
        List<SubjectRatingVo> list = new ArrayList<SubjectRatingVo>();
        for (SubjectRatingEntity rating : getDaoFactory().getSubjectRatingDao().
                getByEvaluatedObjectId(em, subjectId)) {
            list.add(rating.toVo());
        }
        return list;

    }

    /**
     * Returns a subject rating associated with the specified user, identified
     * by userId, and to the specified subject, identified by subjectId.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity associated with the specified user and
     * subject
     */
    public SubjectRatingVo getBySubjectIdAndUserId(EntityManager em,
            long subjectId, long userId)
            throws DataBaseException {
        SubjectRatingEntity subjectRatingEntity = getDaoFactory().getSubjectRatingDao().
                getByEvaluatedObjectIdAndUserId(em, subjectId, userId);

        if (subjectRatingEntity == null) {
            return null;
        }

        return subjectRatingEntity.toVo();
    }

    /**
     * Returns a summary of the ratings associated with a specified subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object with the information associated with the
     * ratings corresponding to the specified subject
     */
    public RatingSummaryVo getSummary(EntityManager em, long subjectId)
            throws DataBaseException {
        return getDaoFactory().getSubjectRatingDao().getSummary(em, subjectId);
    }
}
