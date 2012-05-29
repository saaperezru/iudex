package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import org.xtremeware.iudex.dao.BinaryRatingDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public abstract class SQLBinaryRatingDao<E extends Entity> implements BinaryRatingDao<E>, CrudDao<E> {

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
    public List<E> getByUserId(EntityManager entityManager, Long userId) 
            throws DataBaseException {
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
    
    protected void checkEntityManager(EntityManager entityManager) throws DataBaseException {
        if (entityManager == null) {
            throw new DataBaseException("entityManager.null");
        }
    }
    
    protected abstract Class getEntityClass();

    protected abstract String getQueryForEvaluatedObjectId();

    protected abstract String getQueryForEvaluatedObjectIdAndUserId();

    protected abstract String getQueryForRatingByUserId();

    protected abstract String getQueryForPositiveRating();

    protected abstract String getQueryForNegativeRating();
    
    /**
     * Returns the same received Entity after being persisted in the
     * EntityManager entityManager
     *
     * @param entityManager EntityManager with which the entity will be persisted
     * @param entity Entity that will be persisted
     * @return The received entity after being persisted
     */
    @Override
    public E persist(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            entityManager.persist(entity);
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
        return entity;
    }

    /**
     * Returns the received Entity after being merged in the EntityManager entityManager
     *
     * @param entityManager EntityManager with which the entity will be persisted
     * @param entity Entity that will be merged
     * @return The received entity after being merged and persisted
     */
    @Override
    public E merge(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.merge(entity);
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Deletes the entity identified by id within the received EntityManager
     *
     * @param entityManager EntityManager with which the entity will be deleted
     */
    @Override
    public void remove(EntityManager entityManager, long id)
            throws DataBaseException {
        checkEntityManager(entityManager);
        E entity = getById(entityManager, id);
        if (entity == null) {
            throw new DataBaseException("entity.notFound");
        }
        try {
            entityManager.remove(entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

    }

    /**
     * Returns an Entity whose Id is equal to the received id.
     *
     * @param entityManager EntityManager within which the entity will be searched
     * @param entity Entity that will be searched
     * @return The received entity after being merged and persisted
     */
    @Override
    public E getById(EntityManager entityManager, long id)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return (E) entityManager.find(getEntityClass(), id);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }


    }
}

