package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;

/**
 * DAO for the SubjectRatingVo.
 * 
 * @author josebermeo
 */
public interface SubjectRatingDao<E> extends CrudDao<SubjectRatingVo, E> {

    /**
     * Returns a list of SubjectRatings Value objects given the subject
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @return list of SubjectRatingVo
     */
    public List<SubjectRatingVo> getBySubjectId(DataAccessAdapter<E> em, Long subjectId);

    /**
     * Returns a rating given by a user, identified by userId, to a subject,
     * identified by subjectId.
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity
     */
    public SubjectRatingVo getBySubjectIdAndUserId(DataAccessAdapter<E> em, Long subjectId, Long userId);

    /**
     * Returns a list of SubjectRatings value Objects given the user
     *
     * @param em the DataAccessAdapter
     * @param userId user id
     * @return list of SubjectRatingVo
     */
    public List<SubjectRatingVo> getByUserId(DataAccessAdapter<E> em, Long userId);

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    public RatingSummaryVo getSummary(DataAccessAdapter<E> em, Long subjectId);
}
