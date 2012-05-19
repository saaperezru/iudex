package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.BinaryRatingDaoInterface;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public abstract class BinaryRatingDao<E extends Entity> extends CrudDao<E>
        implements BinaryRatingDaoInterface<E> {

    /**
     * Returns a list of rating entities given the evaluated object
     *
     * @param entityManager the entity manager
     * @param commentId comment id
     * @return list of rating entities
     */
    @Override
    public List<E> getByEvaluatedObjectId(EntityManager entityManager,
            Long evaluatedObjectId) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery(getQueryForEvaluatedObjectId(), getEntityClass()).
                    setParameter("evaluatedObjectId", evaluatedObjectId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a rating given by a user, identified by userId, to a evaluated
     * object, identified by evaluatedObjectId.
     *
     * @param entityManager the entity manager
     * @param evaluatedObjectId evaluated object id
     * @param userId user id
     * @return a rating entity
     */
    @Override
    public E getByEvaluatedObjectIdAndUserId(EntityManager entityManager,
            Long evaluatedObjectId, Long userId) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return (E) entityManager.createNamedQuery(getQueryForEvaluatedObjectIdAndUserId(), getEntityClass()).
                    setParameter("evaluatedObjectId", evaluatedObjectId).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        } catch (Exception exception) {
            throw new DataBaseException(exception.getMessage(), exception.getCause());
        }
    }

    /**
     * Returns a list of rating entities given the user
     *
     * @param entityManager the EntityManager
     * @param userId user id
     * @return list of rating entities
     */
    @Override
    public List<E> getByUserId(EntityManager entityManager,
            Long userId) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery(getQueryForRatingByUserId(), getEntityClass()).
                    setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a summary of the ratings given a evaluated object.
     *
     * @param entityManager the entity manager
     * @param evaluatedObjectId evaluated object id
     * @return a RatingSummaryVo object, null if the COUNTING process of either
     * positive or negative ratings returns no result from the EntityManager.
     */
    @Override
    public RatingSummaryVo getSummary(EntityManager entityManager, Long evaluatedObjectId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        RatingSummaryVo ratingSummaryVo = new RatingSummaryVo();

        try {
            ratingSummaryVo.setPositive(entityManager.createNamedQuery(getQueryForPositiveRating(), Long.class).
                    setParameter("evaluatedObjectId", evaluatedObjectId).getSingleResult().intValue());

            ratingSummaryVo.setNegative(entityManager.createNamedQuery(getQueryForNegativeRating(), Long.class).
                    setParameter("evaluatedObjectId", evaluatedObjectId).getSingleResult().intValue());
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
        return ratingSummaryVo;
    }

    protected abstract String getQueryForEvaluatedObjectId();

    protected abstract String getQueryForEvaluatedObjectIdAndUserId();

    protected abstract String getQueryForRatingByUserId();

    protected abstract String getQueryForPositiveRating();

    protected abstract String getQueryForNegativeRating();
}
