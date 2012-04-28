package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author josebermeo
 */
public interface CrudDao<E extends ValueObject,F> {

    public E persist(DataAccessAdapter<F> em, E vo);

    public E merge(DataAccessAdapter<F> em, E vo);

    public void remove(DataAccessAdapter<F> em, long id);

    public E getById(DataAccessAdapter<F> em, long id);
    
    public void checkDataAccessAdapter(DataAccessAdapter em);
}
