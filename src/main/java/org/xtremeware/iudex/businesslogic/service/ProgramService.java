package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.ProgramDao;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramService extends SimpleCrudService<ProgramVo, ProgramEntity> {

	public ProgramService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Dao<ProgramEntity> getDao() {
		return getDaoFactory().getProgramDao();
	}

	@Override
	public void validateVo(EntityManager em, ProgramVo vo) throws InvalidVoException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
		if (vo.getName() == null) {
			throw new InvalidVoException("String message in the provided ProgramVo cannot be null");
		}
	}

	@Override
	public ProgramEntity voToEntity(EntityManager em, ProgramVo vo) throws InvalidVoException, ExternalServiceConnectionException {
		validateVo(em, vo);
		ProgramEntity entity = new ProgramEntity();
		entity.setId(vo.getId());
		try {
			entity.setName(SecurityHelper.sanitizeHTML(vo.getName()));
		} catch (ExternalServiceConnectionException ex) {
			throw ex;
		}
		return entity;

	}

	/**
	 * Search a program which name contains the given parameter name
	 *
	 * @param em the entity manager
	 * @param name
	 * @return Return a list of <code>ProgramVo></code> objects that contain the given name
	 */
	public List<ProgramVo> getByNameLike(EntityManager em, String name) {
		ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
		for (ProgramEntity entity : ((ProgramDao) getDao()).getByNameLike(em, name)) {
			list.add(entity.toVo());
		}
		return list;
	}
}
