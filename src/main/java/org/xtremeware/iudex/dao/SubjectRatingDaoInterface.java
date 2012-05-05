package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public interface SubjectRatingDaoInterface extends CrudDaoInterface<SubjectRatingEntity> {

    /**
     * Returns a list of SubjectRatings entities given the subject
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return list of SubjectRatingEntity
     */
    public List<SubjectRatingEntity> getBySubjectId(EntityManager em, Long subjectId);

    /**
     * Returns a rating given by a user, identified by userId, to a subject,
     * identified by subjectId.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity
     */
    public SubjectRatingEntity getBySubjectIdAndUserId(EntityManager em, Long subjectId, Long userId);

    /**
     * Returns a list of SubjectRatings entities given the user
     *
     * @param em the entity manager
     * @param userId user id
     * @return list of SubjectRatingEntity
     */
    public List<SubjectRatingEntity> getByUserId(EntityManager em, Long userId);

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the entity manager
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    public RatingSummaryVo getSummary(EntityManager em, Long subjectId);
}
