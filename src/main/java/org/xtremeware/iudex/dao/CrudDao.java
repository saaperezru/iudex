package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface CrudDao<E extends Entity> {

    public E persist(EntityManager entityManager, E entity)
            throws DataBaseException;

    public E merge(EntityManager entityManager, E entity)
            throws DataBaseException;

    public void remove(EntityManager entityManager, long entityId)
            throws DataBaseException;

    public E getById(EntityManager entityManager, long entityId)
            throws DataBaseException;
}
