package org.xtremeware.iudex.dao.sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

public abstract class SQLCrudDao<E extends Entity> implements CrudDao<E> {

    private Remove remove;

    public SQLCrudDao(Remove remove) {
        this.remove = remove;
    }

    @Override
    public E persist(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            entityManager.persist(entity);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new DataBaseException("entity.exists", ex.getCause());
            }
            throw ex;
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
        return entity;
    }

    @Override
    public E merge(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.merge(entity);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new DataBaseException("entity.exists", ex.getCause());
            }
            throw ex;
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void remove(EntityManager entityManager, long entityId)
            throws DataBaseException {

        checkEntityManager(entityManager);

        E entity = getById(entityManager, entityId);
        if (entity == null) {
            throw new DataBaseException("entity.notFound");
        }
        try {
            getRemove().remove(entityManager, entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    private Remove getRemove() {
        return remove;
    }

    @Override
    public E getById(EntityManager entityManager, long entityId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return (E) entityManager.find(getEntityClass(), entityId);
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
