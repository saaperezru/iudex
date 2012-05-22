package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface Remove {

    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException;
}
