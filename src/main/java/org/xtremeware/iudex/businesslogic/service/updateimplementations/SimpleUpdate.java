package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleUpdate<E extends Entity> implements Update<E> {

    private CrudDao<E> dao;

    public SimpleUpdate(CrudDao<E> dao) {
        this.dao = dao;
    }

    @Override
    public E update(EntityManager entityManager, E entity) throws DataBaseException {
        if (getDao().getById(entityManager, entity.getId()) != null) {
            return dao.merge(entityManager, entity);
        } else {
            return null;
        }
    }

    public CrudDao<E> getDao() {
        return dao;
    }
}
