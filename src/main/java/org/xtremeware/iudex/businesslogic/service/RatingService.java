package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.ReadInterface;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.BinaryRatingDaoInterface;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.RatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public abstract class RatingService<E extends BinaryRatingVo, F extends Entity<E>> {

    private AbstractDaoFactory daoFactory;
    private ReadInterface<F> readInterface;
    private RemoveInterface removeInterface;
    private BinaryRatingDaoInterface<F> dao;

    public RatingService(AbstractDaoFactory daoFactory,
            ReadInterface readInterface,
            RemoveInterface removeInterface,
            BinaryRatingDaoInterface<F> dao) {
        this.daoFactory = daoFactory;
        this.readInterface = readInterface;
        this.removeInterface = removeInterface;
        this.dao = dao;
    }

    protected AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }

    private ReadInterface<F> getReadInterface() {
        return readInterface;
    }

    private RemoveInterface getRemoveInterface() {
        return removeInterface;
    }

    private BinaryRatingDaoInterface<F> getDao() {
        return dao;
    }

    public E create(EntityManager entityManager, E vo) throws MultipleMessagesException,
            ExternalServiceConnectionException,
            DataBaseException,
            DuplicityException {
        validateVo(entityManager, vo);
        try {
            F ratingEntity = getDao().
                    getByEvaluatedObjectIdAndUserId(entityManager, vo.getEvaluatedObjectId(),
                    vo.getUserId());
            if (ratingEntity == null) {
                return getDao().persist(entityManager, voToEntity(entityManager, vo)).toVo();
            } else {
                ((RatingEntity) ratingEntity).setValue(vo.getValue());
                return ratingEntity.toVo();
            }
        } catch (DataBaseException ex) {
            if (ex.getMessage().equals("entity.exists")) {
                throw new DuplicityException("entity.exists", ex.getCause());
            } else {
                throw ex;
            }
        }

    }

    public E getById(EntityManager em, long id) throws DataBaseException {
        F result = getReadInterface().getById(em, id);
        if (result == null) {
            return null;
        } else {
            return result.toVo();
        }
    }

    public void remove(EntityManager em, long id)
            throws DataBaseException {
        getRemoveInterface().remove(em, id);
    }
    
    public List<E> getByEvaluatedObjectId(EntityManager entityManager, long evaluatedObjectId)
            throws DataBaseException {
        List<E> list = new ArrayList<E>();
        for (F rating : getDao().
                getByEvaluatedObjectId(entityManager, evaluatedObjectId)) {
            list.add(rating.toVo());
        }
        return list;
    }

    public E getByEvaluatedObjectAndUserId(EntityManager entityManager,
            long evaluatedObjectId, long userId)throws DataBaseException {
        F rating = getDao().
                getByEvaluatedObjectIdAndUserId(entityManager, evaluatedObjectId, userId);

        if (rating == null) {
            return null;
        }

        return rating.toVo();
    }

    public RatingSummaryVo getSummary(EntityManager entityManager, long evaluatedObjectId)
            throws DataBaseException {
        return getDao().getSummary(entityManager, evaluatedObjectId);
    }

    public abstract void validateVo(EntityManager em, E vo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    public abstract F voToEntity(EntityManager em, E vo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;
}
