package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.jpa.JpaProgramDao;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsService extends SimpleCrudService<ProgramVo> {

	public ProgramsService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	protected CrudDao<ProgramVo,?> getDao() {
		return getDaoFactory().getProgramDao();
	}

	@Override
	public void validateVo(DataAccessAdapter em, ProgramVo vo) throws InvalidVoException, ExternalServiceConnectionException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
                if (vo == null) {
                    throw new InvalidVoException("Null ProgramVo");
                }
		if (vo.getName() == null) {
			throw new InvalidVoException("String message in the provided ProgramVo cannot be null");
		}
                vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
	}

	/**
	 * Search a program which name contains the given parameter name
	 *
	 * @param em the entity manager
	 * @param name
	 * @return Return a list of <code>ProgramVo></code> objects that contain the given name
	 */
	public List<ProgramVo> getByNameLike(DataAccessAdapter em, String name) {
		return ((JpaProgramDao) getDao()).getByNameLike(em, name);
	}
}
