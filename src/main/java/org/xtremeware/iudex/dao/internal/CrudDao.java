package org.xtremeware.iudex.dao.internal;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;

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
    public E persist(EntityManager em, E entity) {
        checkEntityManager(em);
        em.persist(entity);
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
    public E merge(EntityManager em, E entity) {
        checkEntityManager(em);
        return em.merge(entity);
    }

    /**
     * Deletes the entity identified by id within the received EntityManager
     *
     * @param em EntityManager with which the entity will be deleted
     */
    @Override
    public void remove(EntityManager em, long id) {
        checkEntityManager(em);
        E entity = getById(em, id);
        if (entity == null) {
            throw new NoResultException("No entity found for id " + String.valueOf(id) + "while triying to delete the associated record");
        }
        em.remove(entity);

    }

    /**
     * Returns an Entity whose Id is equal to the received id.
     *
     * @param em EntityManager within which the entity will be searched
     * @param entity Entity that will be searched
     * @return The received entity after being merged and persisted
     */
    @Override
    public E getById(EntityManager em, long id) {
        checkEntityManager(em);
        return (E) em.find(getEntityClass(), id);


    }

    protected void checkEntityManager(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
    }

    protected abstract Class getEntityClass();
}
