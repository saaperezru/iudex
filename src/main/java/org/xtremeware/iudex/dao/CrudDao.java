package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface CrudDao<E extends Entity> {

    void create(EntityManager entityManager, E entity)
            throws DataBaseException;

    E read(EntityManager entityManager, long entityId)
            throws DataBaseException;

    E update(EntityManager entityManager, E entity)
            throws DataBaseException;

    void delete(EntityManager entityManager, long entityId)
            throws DataBaseException;
}
