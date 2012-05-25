package org.xtremeware.iudex.dao.sql.removeimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleRemove<E extends Entity> implements Remove{

    @Override
    public void remove(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        try {
            entityManager.remove(entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }
    
}
