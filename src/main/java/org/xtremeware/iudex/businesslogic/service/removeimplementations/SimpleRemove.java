package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public class SimpleRemove<E extends Entity> implements RemoveInterface {

    private CrudDaoInterface<E> dao;

    public SimpleRemove(CrudDaoInterface<E> dao) {
        this.dao = dao;
    }

    @Override
    public void remove(EntityManager em, Long id) {
        getDao().remove(em, id);
    }

    public CrudDaoInterface<E> getDao() {
        return dao;
    }
}
