package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.ProfessorRatingDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

public class ProfessorRatingsService extends SimpleCrudService<ProfessorRatingVo, ProfessorRatingEntity> {

	public ProfessorRatingsService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Dao<ProfessorRatingEntity> getDao() {
		return getDaoFactory().getProfessorRatingDao();
	}

	@Override
	public void validateVo(EntityManager em, ProfessorRatingVo vo) throws InvalidVoException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
		if (vo.getValue() > 1 || vo.getValue() < -1) {
			throw new InvalidVoException("int Value in the provided ProfessorRatingVo must be less than 1 and greater than -1");
		}
		if (vo.getProfessor() == null) {
			throw new InvalidVoException("Long professorId in the provided ProfessorRatingVo cannot be null");
		}
		if (vo.getUser() == null) {
			throw new InvalidVoException("Long userId in the provided ProfessorRatingVo cannot be null");
		}

		ProfessorEntity professor = getDaoFactory().getProfessorDao().getById(em, vo.getProfessor());
		if (professor == null) {
			throw new InvalidVoException("Long professorId in the provided ProfessorRatingVo must correspond to an existing subject entity in the database");
		}

		UserEntity user = getDaoFactory().getUserDao().getById(em, vo.getUser());
		if (user == null) {
			throw new InvalidVoException("Long userId in the provided ProfessorRatingVo must correspond to an existing user entity in the database");
		}

	}

	@Override
	public ProfessorRatingEntity voToEntity(EntityManager em, ProfessorRatingVo vo) throws InvalidVoException {
		validateVo(em, vo);
		ProfessorRatingEntity entity = new ProfessorRatingEntity();
		entity.setId(vo.getId());
		entity.setProfessor(getDaoFactory().getProfessorDao().getById(em, vo.getProfessor()));
		entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUser()));
		entity.setValue(vo.getValue());
		return entity;
	}

	/**
	 * Professor ratings associated with a given professor Id
	 *
	 * @param em the entity manager
	 * @param professorId Professor's ID
	 * @return A list of the ratings associated with the specified professor
	 */
	public List<ProfessorRatingVo> getByProfessorId(EntityManager em, long professorId) {
		ArrayList<ProfessorRatingVo> list = new ArrayList<ProfessorRatingVo>();
		for (ProfessorRatingEntity entity : ((ProfessorRatingDao) getDao()).getByProfessorId(em, professorId)) {
			list.add(entity.toVo());
		}
		return list;

	}

	/**
	 * Looks for professor ratings associated with the professor and user
	 * specified by the given ids.
	 *
	 * @param em the entity manager
	 * @param professorId Professor's ID
	 * @param userId Student's ID
	 * @return A
	 * <code>ProfessorRatingVo</code> associated with the given professor
	 * and user ids
	 */
	public ProfessorRatingVo getByProfessorIdAndUserId(EntityManager em, long professorId, long userId) {
		return ((ProfessorRatingDao) getDao()).getByProfessorIdAndUserId(em, professorId, userId).toVo();
	}

	/**
	 * Calculates the professor rating summary 
	 *
	 * @param em the entity manager
	 * @param professorId Professor's ID
	 * @return A value object containing the number of times the specified
	 * professor has obtained positive and negative ratings
	 */
	public RatingSummaryVo getSummary(EntityManager em, long professorId) {
		return ((ProfessorRatingDao) getDao()).getSummary(em, professorId);

	}
}
