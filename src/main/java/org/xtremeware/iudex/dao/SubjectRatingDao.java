package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;

/**
 *
 * @author josebermeo
 */
public interface SubjectRatingDao<E> extends CrudDao<SubjectRatingVo, E> {

    public List<SubjectRatingVo> getBySubjectId(DataAccessAdapter<E> em, Long subjectId);

    public SubjectRatingVo getBySubjectIdAndUserId(DataAccessAdapter<E> em, Long subjectId, Long userId);

    public List<SubjectRatingVo> getByUserId(DataAccessAdapter<E> em, Long userId);

    public RatingSummaryVo getSummary(DataAccessAdapter<E> em, Long subjectId);
}
