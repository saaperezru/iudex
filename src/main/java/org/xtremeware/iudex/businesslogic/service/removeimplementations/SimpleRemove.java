package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.dao.Remove;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleRemove<E extends Entity> implements Remove {

    private CrudDao<E> dao;

    public SimpleRemove(CrudDao<E> dao) {
        this.dao = dao;
    }

    @Override
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        getDao().remove(entityManager, entityId);
    }

    public CrudDao<E> getDao() {
        return dao;
    }
}
