package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface Read<E extends Entity> {

    E getById(EntityManager em, long entityId)
            throws DataBaseException;
}
