package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface RemoveInterface {

    public void remove(EntityManager em, Long id)
            throws DataBaseException;
}
