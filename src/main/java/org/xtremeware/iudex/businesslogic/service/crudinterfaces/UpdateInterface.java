package org.xtremeware.iudex.businesslogic.service.crudinterfaces;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public interface UpdateInterface<E extends Entity> {

    public E update(EntityManager em, E enetity);
}
