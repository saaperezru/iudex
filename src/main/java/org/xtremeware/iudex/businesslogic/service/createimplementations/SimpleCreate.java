package org.xtremeware.iudex.businesslogic.service.createimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SimpleCreate<E extends Entity> implements Create<E>{
    
    private CrudDao<E> crudDao;

    public SimpleCreate(CrudDao<E> dao) {
        this.crudDao = dao;
    }

    @Override
    public E create(EntityManager entityManager, E entity)
            throws DataBaseException, DuplicityException {
        getCrudDao().create(entityManager, entity);
        return entity;
    }

    private CrudDao<E> getCrudDao(){
        return crudDao;
    }
}
