package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.CourseVo;

/**
 *
 * @author josebermeo
 */
public interface CourseDao<E> extends CrudDao<CourseVo, E> {

    public List<CourseVo> getByProfessorId(DataAccessAdapter<E> em, long professorId)throws DataAccessException;

    public List<CourseVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId)throws DataAccessException;

    public List<CourseVo> getByPeriodId(DataAccessAdapter<E> em, long periodId)throws DataAccessException;

    public List<CourseVo> getByProfessorIdAndSubjectId(DataAccessAdapter<E> em, long professorId, long subjectId)throws DataAccessException;

    public List<CourseVo> getCoursesByProfessorNameLikeAndSubjectNameLike(DataAccessAdapter<E> em, String professorName, String subjectName, Long periodId)throws DataAccessException;
}
