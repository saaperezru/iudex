package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.jpa.JpaProfessorRatingDao;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.UserVo;

public class ProfessorRatingsService extends SimpleCrudService<ProfessorRatingVo> {

    public ProfessorRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    protected CrudDao<ProfessorRatingVo, ?> getDao() {
        return getDaoFactory().getProfessorRatingDao();
    }

    @Override
    public void validateVo(DataAccessAdapter em, ProfessorRatingVo vo) throws InvalidVoException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null ProfessorRatingVo");
        }
        if (vo.getValue() > 1 || vo.getValue() < -1) {
            throw new InvalidVoException("int Value in the provided ProfessorRatingVo must be less than 1 and greater than -1");
        }
        if (vo.getEvaluetedObjectId() == null) {
            throw new InvalidVoException("Long professorId in the provided ProfessorRatingVo cannot be null");
        }
        if (vo.getUser() == null) {
            throw new InvalidVoException("Long userId in the provided ProfessorRatingVo cannot be null");
        }

        ProfessorVo professor = (ProfessorVo) getDaoFactory().getProfessorDao().getById(em, vo.getEvaluetedObjectId());
        if (professor == null) {
            throw new InvalidVoException("Long professorId in the provided ProfessorRatingVo must correspond to an existing subject entity in the database");
        }

        UserVo user = (UserVo) getDaoFactory().getUserDao().getById(em, vo.getUser());
        if (user == null) {
            throw new InvalidVoException("Long userId in the provided ProfessorRatingVo must correspond to an existing user entity in the database");
        }

    }

    /**
     * Professor ratings associated with a given professor Id
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list of the ratings associated with the specified professor
     */
    public List<ProfessorRatingVo> getByProfessorId(DataAccessAdapter em, long professorId) {
        return ((JpaProfessorRatingDao) getDao()).getByProfessorId(em, professorId);
    }

    /**
     * Looks for professor ratings associated with the professor and user
     * specified by the given ids.
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return A
     * <code>ProfessorRatingVo</code> associated with the given professor and
     * user ids
     */
    public ProfessorRatingVo getByProfessorIdAndUserId(DataAccessAdapter em, long professorId, long userId) {
        return ((JpaProfessorRatingDao) getDao()).getByProfessorIdAndUserId(em, professorId, userId);
    }

    /**
     * Calculates the professor rating summary
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    public RatingSummaryVo getSummary(DataAccessAdapter em, long professorId) {
        return ((JpaProfessorRatingDao) getDao()).getSummary(em, professorId);

    }
}
