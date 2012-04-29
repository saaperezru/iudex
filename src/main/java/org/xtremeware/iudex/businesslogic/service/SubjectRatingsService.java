package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.jpa.JpaSubjectRatingDao;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;
import org.xtremeware.iudex.vo.SubjectVo;
import org.xtremeware.iudex.vo.UserVo;

public class SubjectRatingsService extends SimpleCrudService<SubjectRatingVo> {

    public SubjectRatingsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    protected CrudDao<SubjectRatingVo, ?> getDao() {
        return getDaoFactory().getSubjectRatingDao();
    }

    @Override
    public void validateVo(DataAccessAdapter em, SubjectRatingVo vo) throws InvalidVoException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null SubjectRatingVo");
        }
        if (vo.getValue() > 1 || vo.getValue() < -1) {
            throw new InvalidVoException("int Value in the provided SubjectRatingVo must be less than 1 and greater than -1");
        }
        if (vo.getEvaluetedObjectId() == null) {
            throw new InvalidVoException("Long subjectId in the provided SubjectRatingVo cannot be null");
        }
        if (vo.getUser() == null) {
            throw new InvalidVoException("Long userId in the provided SubjectRatingVo cannot be null");
        }

        SubjectVo subject = (SubjectVo) getDaoFactory().getSubjectDao().getById(em, vo.getEvaluetedObjectId());
        if (subject == null) {
            throw new InvalidVoException("Long subjectId in the provided SubjectRatingVo must correspond to an existing subject entity in the database");
        }

        UserVo user = (UserVo) getDaoFactory().getUserDao().getById(em, vo.getUser());
        if (user == null) {
            throw new InvalidVoException("Long userId in the provided SubjectRatingVo must correspond to an existing user entity in the database");
        }
    }

    /**
     * Returns a list of SubjectRatings value objects corresponding to the
     * specified subject id
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingVo instances associated with the specified
     * subject
     */
    public List<SubjectRatingVo> getBySubjectId(DataAccessAdapter em, long subjectId) {
        return ((JpaSubjectRatingDao) getDao()).getBySubjectId(em, subjectId);
    }

    /**
     * Returns a subject rating associated with the specified user, identified
     * by userId, and to the specified subject, identified by subjectId.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity associated with the specified user and
     * subject
     */
    public SubjectRatingVo getBySubjectIdAndUserId(DataAccessAdapter em, long subjectId, long userId) {
        return ((JpaSubjectRatingDao) getDao()).getBySubjectIdAndUserId(em, subjectId, userId);
    }

    /**
     * Returns a summary of the ratings associated with a specified subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object with the information associated with the
     * ratings corresponding to the specified subject
     */
    public RatingSummaryVo getSummary(DataAccessAdapter em, long subjectId) {
        return ((JpaSubjectRatingDao) getDao()).getSummary(em, subjectId);
    }
}
