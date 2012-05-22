package org.xtremeware.iudex.dao.sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

public abstract class SQLCrudDao<E extends Entity> implements CrudDao<E> {

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
        } catch (PersistenceException ex) {
            // TODO: Resolve this hibernate coupling
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new DataBaseException("entity.exists", ex.getCause());
            }
            throw ex;
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
        } catch (PersistenceException ex) {
            // TODO: Resolve this hibernate coupling
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new DataBaseException("entity.exists", ex.getCause());
            }
            throw ex;
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

    protected void checkEntityManager(EntityManager entityManager) throws DataBaseException {
        if (entityManager == null) {
            throw new DataBaseException("entityManager.null");
        }
    }

    protected abstract Class getEntityClass();
}
