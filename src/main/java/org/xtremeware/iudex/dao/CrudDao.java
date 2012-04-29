package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author josebermeo
 */
public interface CrudDao<E extends ValueObject,F> {

    public E persist(DataAccessAdapter<F> em, E vo)throws DataAccessException;

    public E merge(DataAccessAdapter<F> em, E vo)throws DataAccessException;

    public void remove(DataAccessAdapter<F> em, long id)throws DataAccessException;

    public E getById(DataAccessAdapter<F> em, long id)throws DataAccessException;
    
    public void checkDataAccessAdapter(DataAccessAdapter em) throws DataAccessException;
}
