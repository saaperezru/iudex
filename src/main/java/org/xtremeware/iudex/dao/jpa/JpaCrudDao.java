package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.vo.ValueObject;

public abstract class JpaCrudDao<E extends ValueObject, F extends Entity<E>> implements CrudDao<E, EntityManager> {

    /**
     * Returns the same received ValueObject after being persisted in the
     * EntityManager em
     *
     * @param em DataAccessAdapter with which the entity will be persisted
     * @param vo ValueObject that will be persisted
     * @return The received entity after being persisted
     */
    @Override
    public E persist(DataAccessAdapter<EntityManager> em, E vo) {
        checkDataAccessAdapter(em);
        em.getDataAccess().persist(voToEntity(em, vo));
        return vo;
    }

    /**
     * Returns the received ValueObject after being merged in the EntityManager
     * em
     *
     * @param em DataAccessAdapter with which the entity will be persisted
     * @param vo ValueObject that will be merged
     * @return The received ValueObject after being merged and persisted
     */
    @Override
    public E merge(DataAccessAdapter<EntityManager> em, E vo) {
        checkDataAccessAdapter(em);
        return (E) ((F) em.getDataAccess().merge(voToEntity(em, vo))).toVo();
    }

    private F getEntityById(DataAccessAdapter<EntityManager> em, long id) {
        checkDataAccessAdapter(em);
        return (F) em.getDataAccess().find(getEntityClass(), id);
    }

    /**
     * Deletes the ValueObject identified by id within the received
     * DataAccessAdapter
     *
     * @param em DataAccessAdapter with which the entity will be deleted
     */
    @Override
    public void remove(DataAccessAdapter<EntityManager> em, long id) {
        F entity = getEntityById(em, id);
        if (entity == null) {
            throw new NoResultException("No entity found for id " + String.valueOf(id) + "while triying to delete the associated record");
        }
        em.getDataAccess().remove(entity);
    }

    /**
     * Returns an ValueObject whose Id is equal to the received id.
     *
     * @param em EntityManager within which the entity will be searched
     * @param entity ValueObject that will be searched
     * @return The received entity after being merged and persisted
     */
    /**
     * Returns an ValueObject whose Id is equal to the received id.
     *
     * @param em DataAccessAdapter within which the entity will be searched
     * @param id of the ValueObject that will be searched
     * @return The received ValueObject after being merged and persisted
     */
    @Override
    public E getById(DataAccessAdapter<EntityManager> em, long id) {
        return (E) (getEntityById(em, id)).toVo();
    }

    @Override
    public void checkDataAccessAdapter(DataAccessAdapter em) {
        if (em == null) {
            throw new IllegalArgumentException("DataAccessAdapter em cannot be null");
        }
        if (em.getDataAccess() == null) {
            throw new IllegalArgumentException("DataAccess cannot be null");
        }
    }
    
    protected List<E> entitiesToVos(List<F> list){
        ArrayList<E> arrayList = new ArrayList<E>();
        for (F entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }

    protected abstract F voToEntity(DataAccessAdapter<EntityManager> em, E vo);

    protected abstract Class getEntityClass();
}