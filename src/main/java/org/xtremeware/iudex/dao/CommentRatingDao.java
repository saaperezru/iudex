package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public interface CommentRatingDao<E> extends CrudDao<CommentRatingVo, E> {

    public List<CommentRatingVo> getByCommentId(DataAccessAdapter<E> em, Long commentId);

    public CommentRatingVo getByCommentIdAndUserId(DataAccessAdapter<E> em, Long commentId, Long userId);

    public List<CommentRatingVo> getByUserId(DataAccessAdapter<E> em, Long userId);

    public RatingSummaryVo getSummary(DataAccessAdapter<E> em, Long commentId);
}
