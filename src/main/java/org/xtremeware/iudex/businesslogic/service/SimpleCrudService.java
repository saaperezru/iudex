package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class SimpleCrudService<E extends ValueObject> extends CrudService<E> {

    protected CrudDao<E, ?> dao;

    public SimpleCrudService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    protected abstract CrudDao<E, ?> getDao();

    public E update(DataAccessAdapter em, E vo) throws InvalidVoException, ExternalServiceConnectionException {
        validateVo(em, vo);
        return getDao().merge(em, vo);

    }

    public E getById(DataAccessAdapter em, long id) {
        return getDao().getById(em, id);
    }

    public E create(DataAccessAdapter em, E vo) throws InvalidVoException, ExternalServiceConnectionException {
        validateVo(em, vo);
        return getDao().persist(em, vo);
    }

    public void remove(DataAccessAdapter em, long id) {
        getDao().remove(em, id);
    }

    public abstract void validateVo(DataAccessAdapter em, E vo) throws InvalidVoException, ExternalServiceConnectionException;
}
