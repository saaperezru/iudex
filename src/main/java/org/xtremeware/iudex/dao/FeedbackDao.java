package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public interface FeedbackDao<E> extends CrudDao<FeedbackVo, E> {

    public List<FeedbackVo> getByTypeId(DataAccessAdapter<E> em, long feedbackTypeId);

    public List<FeedbackVo> getByContentLike(DataAccessAdapter<E> em, String query);
}
