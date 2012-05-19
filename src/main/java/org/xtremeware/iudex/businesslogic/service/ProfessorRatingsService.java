package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.createimplementations.ProfessorRatingCreate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

public class ProfessorRatingsService extends CrudService<ProfessorRatingVo, ProfessorRatingEntity> {

    public ProfessorRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new ProfessorRatingCreate(daoFactory.
                getProfessorRatingDao()),
                new SimpleRead<ProfessorRatingEntity>(daoFactory.
                getProfessorRatingDao()),
                new SimpleUpdate<ProfessorRatingEntity>(daoFactory.
                getProfessorRatingDao()),
                new SimpleRemove<ProfessorRatingEntity>(daoFactory.
                getProfessorRatingDao()));
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
        } else if (getDaoFactory().getProfessorDao().getById(em, vo.
                getEvaluatedObjectId()) == null) {
            multipleMessageException.addMessage(
                    "professorRating.professorId.element.notFound");
        }
        if (vo.getUserId() == null) {
            multipleMessageException.addMessage(
                    "professorRating.userId.null");
        } else if (getDaoFactory().getUserDao().getById(em, vo.getUserId()) ==
                null) {
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
        entity.setProfessor(getDaoFactory().getProfessorDao().getById(em, vo.
                getEvaluatedObjectId()));
        entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUserId()));
        entity.setValue(vo.getValue());
        return entity;
    }

    /**
     * Professor ratings associated with a given professor Id
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list of the ratings associated with the specified professor
     */
    public List<ProfessorRatingVo> getByProfessorId(EntityManager em,
            long professorId)
            throws DataBaseException {
        ArrayList<ProfessorRatingVo> list = new ArrayList<ProfessorRatingVo>();
        for (ProfessorRatingEntity entity : getDaoFactory().
                getProfessorRatingDao().getByEvaluatedObjectId(em, professorId)) {
            list.add(entity.toVo());
        }
        return list;

    }

    /**
     * Looks for professor ratings associated with the professor and user
     * specified by the given ids.
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return A
     * <code>ProfessorRatingVo</code> associated with the given professor and
     * user ids
     */
    public ProfessorRatingVo getByProfessorIdAndUserId(EntityManager em,
            long professorId, long userId)
            throws DataBaseException {
        ProfessorRatingEntity pre = getDaoFactory().getProfessorRatingDao().
                getByEvaluatedObjectIdAndUserId(em, professorId, userId);
        if(pre == null){
            return null;
        } else {
            return pre.toVo();
        }
        
    }

    /**
     * Calculates the professor rating summary
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    public RatingSummaryVo getSummary(EntityManager em, long professorId)
            throws DataBaseException {
        return getDaoFactory().getProfessorRatingDao().getSummary(em,
                professorId);

    }
}
