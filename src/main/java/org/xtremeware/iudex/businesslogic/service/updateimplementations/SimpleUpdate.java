package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.UpdateInterface;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public class SimpleUpdate<E extends Entity> implements UpdateInterface<E> {

    private CrudDaoInterface<E> dao;

    public SimpleUpdate(CrudDaoInterface<E> dao) {
        this.dao = dao;
    }

    @Override
    public E update(EntityManager em, E enetity) {
        return getDao().merge(em, enetity);
    }

    public CrudDaoInterface<E> getDao() {
        return dao;
    }
}
