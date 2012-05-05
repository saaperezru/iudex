package org.xtremeware.iudex.dao;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public interface CrudDaoInterface<E extends Entity> {

    public E persist(EntityManager em, E entity);

    public E merge(EntityManager em, E entity);

    public void remove(EntityManager em, long id);

    public E getById(EntityManager em, long id);

    public void checkEntityManager(EntityManager em);
}
