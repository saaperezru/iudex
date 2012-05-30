package org.xtremeware.iudex.businesslogic.service.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleDelete<E extends Entity> implements Delete {

    private CrudDao<E> dao;

    public SimpleDelete(CrudDao<E> dao) {
        this.dao = dao;
    }

    @Override
    public void delete(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        getDao().delete(entityManager, entityId);
    }

    public CrudDao<E> getDao() {
        return dao;
    }
}
