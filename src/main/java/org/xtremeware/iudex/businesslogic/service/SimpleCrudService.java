package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class SimpleCrudService<E extends ValueObject, F extends Entity<E>> extends CrudService<E> {

    protected Dao<F> dao;

    public SimpleCrudService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    protected abstract Dao<F> getDao();

    public E update(EntityManager em, E vo) throws InvalidVoException {
        validateVo(vo);
        return getDao().merge(em, voToEntity(vo)).toVo();

    }

    public E getById(EntityManager em, long id) {
        return getDao().getById(em, id).toVo();
    }

    public E create(EntityManager em, E vo) throws InvalidVoException {
        validateVo(vo);
        return getDao().persist(em, voToEntity(vo)).toVo();
    }

    public void remove(EntityManager em, long id) {
        getDao().remove(em, id);
    }

    public abstract void validateVo(E vo) throws InvalidVoException;

    public abstract F voToEntity(E vo);
}
