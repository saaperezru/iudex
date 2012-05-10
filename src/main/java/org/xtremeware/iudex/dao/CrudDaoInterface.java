package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface CrudDaoInterface<E extends Entity> {

    public E persist(EntityManager em, E entity)
            throws DataBaseException, DuplicityException;

    public E merge(EntityManager em, E entity)
            throws DataBaseException;

    public void remove(EntityManager em, long id)
            throws DataBaseException;

    public E getById(EntityManager em, long id)
            throws DataBaseException;
}
