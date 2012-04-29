package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.CourseRatingVo;

/**
 *
 * @author josebermeo
 */
public interface CourseRatingDao<E> extends CrudDao<CourseRatingVo, E> {

    public List<CourseRatingVo> getByCourseId(DataAccessAdapter<E> em, Long courseId)throws DataAccessException;

    public CourseRatingVo getByCourseIdAndUserId(DataAccessAdapter<E> em, Long courseId, Long userId)throws DataAccessException;

    public List<CourseRatingVo> getByUserId(DataAccessAdapter<E> em, Long userId)throws DataAccessException;
}
