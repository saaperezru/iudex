package org.xtremeware.iudex.businesslogic.service;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class SimpleCrudService<E extends ValueObject, F extends Entity<E>> extends CrudService<E> {
	
	protected Dao<F> dao;
	
	public SimpleCrudService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}
	
	protected abstract Dao<F> getDao();
	
	public E update(EntityManager em, E vo) throws InvalidVoException, ExternalServiceConnectionException {
		validateVo(em, vo);
		return getDao().merge(em, voToEntity(em, vo)).toVo();
		
	}
	
	public E getById(EntityManager em, long id) {
		F result = getDao().getById(em, id);
		if (result == null) {
			return null;
		} else {
			return result.toVo();
		}
	}
	
	public E create(EntityManager em, E vo) throws InvalidVoException, ExternalServiceConnectionException {
		validateVo(em, vo);
		return getDao().persist(em, voToEntity(em, vo)).toVo();
	}
	
	public void remove(EntityManager em, long id) {
		getDao().remove(em, id);
	}
	
	public abstract void validateVo(EntityManager em, E vo) throws InvalidVoException;
	
	public abstract F voToEntity(EntityManager em, E vo) throws InvalidVoException, ExternalServiceConnectionException;
}
