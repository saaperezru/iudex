package org.xtremeware.iudex.dao.sql.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleDeleteBehavior<E extends Entity> implements Delete{

    @Override
    public void delete(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        try {
            entityManager.remove(entity);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }
    
}
