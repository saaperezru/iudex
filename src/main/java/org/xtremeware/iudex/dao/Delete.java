package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface Delete<E extends Entity> {

    public void delete(EntityManager entityManager, E entity)
            throws DataBaseException;
}
