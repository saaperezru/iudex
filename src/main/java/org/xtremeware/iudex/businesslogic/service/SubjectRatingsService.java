package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.SubjectRatingDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;

public class SubjectRatingsService extends SimpleCrudService<SubjectRatingVo, SubjectRatingEntity> {

	public SubjectRatingsService(AbstractDaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Dao<SubjectRatingEntity> getDao() {
		return getDaoFactory().getSubjectRatingDao();
	}

	@Override
	public void validateVo(EntityManager em, SubjectRatingVo vo) throws InvalidVoException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
                if (vo == null) {
                    throw new InvalidVoException("Null SubjectRatingVo");
                }
		if (vo.getValue() > 1 || vo.getValue() < -1) {
			throw new InvalidVoException("int Value in the provided SubjectRatingVo must be less than 1 and greater than -1");
		}
		if (vo.getSubject() == null) {
			throw new InvalidVoException("Long subjectId in the provided SubjectRatingVo cannot be null");
		}
		if (vo.getUser() == null) {
			throw new InvalidVoException("Long userId in the provided SubjectRatingVo cannot be null");
		}

		SubjectEntity subject = getDaoFactory().getSubjectDao().getById(em, vo.getSubject());
		if (subject == null) {
			throw new InvalidVoException("Long subjectId in the provided SubjectRatingVo must correspond to an existing subject entity in the database");
		}

		UserEntity user = getDaoFactory().getUserDao().getById(em, vo.getUser());
		if (user == null) {
			throw new InvalidVoException("Long userId in the provided SubjectRatingVo must correspond to an existing user entity in the database");
		}
	}

	@Override
	public SubjectRatingEntity voToEntity(EntityManager em, SubjectRatingVo vo) throws InvalidVoException {
		validateVo(em, vo);
		SubjectRatingEntity entity = new SubjectRatingEntity();
		entity.setId(vo.getId());
		entity.setSubject(getDaoFactory().getSubjectDao().getById(em, vo.getUser()));
		entity.setUser(getDaoFactory().getUserDao().getById(em, vo.getUser()));
		entity.setValue(vo.getValue());
		return entity;
	}

	/**
	 * Returns a list of SubjectRatings value objects corresponding to the
	 * specified subject id
	 *
	 * @param em the entity manager
	 * @param subjectId subject id
	 * @return list of SubjectRatingVo instances associated with the
	 * specified subject
	 */
	public List<SubjectRatingVo> getBySubjectId(EntityManager em, long subjectId) {
		List<SubjectRatingVo> list = new ArrayList<SubjectRatingVo>();
		for (SubjectRatingEntity rating : ((SubjectRatingDao) getDao()).getBySubjectId(em, subjectId)) {
			list.add(rating.toVo());
		}
		return list;

	}

	/**
	 * Returns a subject rating associated with the specified user,
	 * identified by userId, and to the specified subject, identified by
	 * subjectId.
	 *
	 * @param em the entity manager
	 * @param subjectId subject id
	 * @param userId user id
	 * @return a SubjectRatingEntity associated with the specified user and
	 * subject
	 */
	public SubjectRatingVo getBySubjectIdAndUserId(EntityManager em, long subjectId, long userId) {
		return ((SubjectRatingDao) getDao()).getBySubjectIdAndUserId(em, subjectId, userId).toVo();
	}

	/**
	 * Returns a summary of the ratings associated with a specified subject.
	 *
	 * @param em the entity manager
	 * @param subjectId subject id
	 * @return a RatingSummaryVo object with the information associated with
	 * the ratings corresponding to the specified subject
	 */
	public RatingSummaryVo getSummary(EntityManager em, long subjectId) {
		return ((SubjectRatingDao) getDao()).getSummary(em, subjectId);
	}
}
