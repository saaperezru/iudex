package org.xtremeware.iudex.businesslogic.service.readimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Read;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleRead<E extends Entity> implements Read<E> {

    private CrudDao<E> dao;

    public SimpleRead(CrudDao<E> dao) {
        this.dao = dao;
    }

    @Override
    public E getById(EntityManager em, long entityId)
            throws DataBaseException {
        return getDao().getById(em, entityId);
    }

    public CrudDao<E> getDao() {
        return dao;
    }
}
