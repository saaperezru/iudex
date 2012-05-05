package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public interface CreateInterface<E extends Entity> {

    public E create(EntityManager em, E entity) throws MaxCommentsLimitReachedException;
}
