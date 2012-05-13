package org.xtremeware.iudex.dao.internal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

public abstract class CrudDao<E extends Entity> implements CrudDaoInterface<E> {

    /**
     * Returns the same received Entity after being persisted in the
     * EntityManager em
     *
     * @param em EntityManager with which the entity will be persisted
     * @param entity Entity that will be persisted
     * @return The received entity after being persisted
     */
    @Override
    public E persist(EntityManager em, E entity) throws DataBaseException{
        checkEntityManager(em);
        try {
            em.persist(entity);
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
     * Returns the received Entity after being merged in the EntityManager em
     *
     * @param em EntityManager with which the entity will be persisted
     * @param entity Entity that will be merged
     * @return The received entity after being merged and persisted
     */
    @Override
    public E merge(EntityManager em, E entity) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.merge(entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Deletes the entity identified by id within the received EntityManager
     *
     * @param em EntityManager with which the entity will be deleted
     */
    @Override
    public void remove(EntityManager em, long id) throws DataBaseException {
        checkEntityManager(em);
        E entity = getById(em, id);
        if (entity == null) {
            throw new DataBaseException("No entity found for id " + String.
                    valueOf(id) +
                    "while triying to delete the associated record");
        }
        try {
            em.remove(entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

    }

    /**
     * Returns an Entity whose Id is equal to the received id.
     *
     * @param em EntityManager within which the entity will be searched
     * @param entity Entity that will be searched
     * @return The received entity after being merged and persisted
     */
    @Override
    public E getById(EntityManager em, long id) throws DataBaseException {
        checkEntityManager(em);
        try {
            return (E) em.find(getEntityClass(), id);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }


    }

    protected void checkEntityManager(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
    }

    protected abstract Class getEntityClass();
}
