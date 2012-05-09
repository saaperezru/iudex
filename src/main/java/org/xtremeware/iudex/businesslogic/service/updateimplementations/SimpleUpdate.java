package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.UpdateInterface;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

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
    public E update(EntityManager em, E entity) throws DataBaseException {
        if (getDao().getById(em, entity.getId()) != null) {
            return dao.merge(em, entity);
        } else {
            return null;
        }
    }

    public CrudDaoInterface<E> getDao() {
        return dao;
    }
}
