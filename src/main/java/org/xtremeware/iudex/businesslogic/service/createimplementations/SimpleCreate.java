package org.xtremeware.iudex.businesslogic.service.createimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.CreateInterface;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CrudDaoInterface;
import org.xtremeware.iudex.entity.Entity;

/**
 *
 * @author josebermeo
 */
public class SimpleCreate<E extends Entity> implements CreateInterface<E>{
    
    private CrudDaoInterface<E> dao;

    public SimpleCreate(CrudDaoInterface<E> dao) {
        this.dao = dao;
    }

    @Override
    public E create(EntityManager em, E entity) {
        return getDao().persist(em, entity);
    }

    public CrudDaoInterface<E> getDao(){
        return dao;
    }
}
