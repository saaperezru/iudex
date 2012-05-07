package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.UpdateInterface;
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
    public E update(EntityManager em, E entity) {
        CrudDaoInterface<E> dao = getDao();
        if (dao.getById(em, entity.getId()) != null) {
            return dao.merge(em, entity);
        } else {
            return null;
        }
    }

    public CrudDaoInterface<E> getDao() {
        return dao;
    }
}
