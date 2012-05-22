package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Read;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.BinaryRatingDao;
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
public abstract class RatingService<F extends Entity<BinaryRatingVo>> {

    private AbstractDaoFactory daoFactory;
    private Read<F> readInterface;
    private Remove removeInterface;
    private BinaryRatingDao<F> dao;

    public RatingService(AbstractDaoFactory daoFactory,
            Read readInterface,
            Remove removeInterface,
            BinaryRatingDao<F> dao) {
        this.daoFactory = daoFactory;
        this.readInterface = readInterface;
        this.removeInterface = removeInterface;
        this.dao = dao;
    }

    protected AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }

    private Read<F> getReadInterface() {
        return readInterface;
    }

    private Remove getRemoveInterface() {
        return removeInterface;
    }

    protected BinaryRatingDao<F> getDao() {
        return dao;
    }

    public BinaryRatingVo create(EntityManager entityManager, BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException, DuplicityException {
        validateVoForCreation(entityManager, binaryRatingVo);
        try {
            F ratingEntity = getDao().getByEvaluatedObjectIdAndUserId(entityManager,
                    binaryRatingVo.getEvaluatedObjectId(), binaryRatingVo.getUserId());
            if (ratingEntity == null) {
                return getDao().persist(entityManager, voToEntity(entityManager,
                        binaryRatingVo)).toVo();
            } else {
                ((RatingEntity) ratingEntity).setValue(binaryRatingVo.getValue());
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

    public BinaryRatingVo getById(EntityManager em, long ratingId) throws DataBaseException {
        F result = getReadInterface().getById(em, ratingId);
        if (result == null) {
            return null;
        } else {
            return result.toVo();
        }
    }

    public void remove(EntityManager em, long ratingId)
            throws DataBaseException {
        getRemoveInterface().remove(em, ratingId);
    }

    public List<BinaryRatingVo> getByEvaluatedObjectId(EntityManager entityManager,
            long evaluatedObjectId) throws DataBaseException {
        List<BinaryRatingVo> list = new ArrayList<BinaryRatingVo>();
        for (F rating : getDao().
                getByEvaluatedObjectId(entityManager, evaluatedObjectId)) {
            list.add(rating.toVo());
        }
        return list;
    }

    public BinaryRatingVo getByEvaluatedObjectAndUserId(EntityManager entityManager,
            long evaluatedObjectId, long userId) throws DataBaseException {
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

    public abstract void validateVoForCreation(EntityManager entityManager,
            BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    public abstract F voToEntity(EntityManager entityManager,
            BinaryRatingVo binaryRatingVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;
}
