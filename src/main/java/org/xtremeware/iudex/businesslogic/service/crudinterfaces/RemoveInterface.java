package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;

/**
 *
 * @author josebermeo
 */
public interface RemoveInterface {

    public void remove(EntityManager em, Long id);
}
