package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

/**
 *
 * @author josebermeo
 */
public interface FeedbackTypeDao<E> extends CrudDao<FeedbackTypeVo, E> {

    public FeedbackTypeVo getByName(DataAccessAdapter<E> em, String name)throws DataAccessException;

    public List<FeedbackTypeVo> getAll(DataAccessAdapter<E> em)throws DataAccessException;
}