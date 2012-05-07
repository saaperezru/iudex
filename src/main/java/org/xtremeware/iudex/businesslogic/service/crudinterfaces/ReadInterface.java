package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public interface ReadInterface<E extends Entity> {

    public E getById(EntityManager em, long id);
}
