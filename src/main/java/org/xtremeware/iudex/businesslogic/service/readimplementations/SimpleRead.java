package org.xtremeware.iudex.businesslogic.service.readimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.ReadInterface;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public class SimpleRead<E extends Entity> implements ReadInterface<E> {

    private CrudDaoInterface<E> dao;

    public SimpleRead(CrudDaoInterface<E> dao) {
        this.dao = dao;
    }

    @Override
    public E getById(EntityManager em, long id) {
        return getDao().getById(em, id);
    }

    public CrudDaoInterface<E> getDao() {
        return dao;
    }
}
