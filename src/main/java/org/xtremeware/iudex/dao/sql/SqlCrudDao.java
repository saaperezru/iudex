package org.xtremeware.iudex.dao.sql;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

public abstract class SqlCrudDao<E extends Entity> implements CrudDao<E> {

    private Delete delete;

    public SqlCrudDao(Delete delete) {
        this.delete = delete;
    }

    @Override
    public void create(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            entityManager.persist(entity);
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public E update(EntityManager entityManager, E entity)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.merge(entity);
        } catch (Exception ex) {
            throw new DataBaseException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void delete(EntityManager entityManager, long entityId)
            throws DataBaseException {

        checkEntityManager(entityManager);

        E entity = read(entityManager, entityId);
        if (entity == null) {
            throw new DataBaseException("entity.notFound");
        }
        try {
            getDelete().delete(entityManager, entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    private Delete getDelete() {
        return delete;
    }

    @Override
    public E read(EntityManager entityManager, long entityId)
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
