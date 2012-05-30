package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface CrudDao<E extends Entity> {

    public void create(EntityManager entityManager, E entity)
            throws DataBaseException;
    
    public E read(EntityManager entityManager, long entityId)
            throws DataBaseException;

    public E update(EntityManager entityManager, E entity)
            throws DataBaseException;

    public void delete(EntityManager entityManager, long entityId)
            throws DataBaseException;

    
}
