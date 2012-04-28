package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.CourseVo;

/**
 *
 * @author josebermeo
 */
public interface CourseDao<E> extends CrudDao<CourseVo, E> {

    public List<CourseVo> getByProfessorId(DataAccessAdapter<E> em, long professorId);

    public List<CourseVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId);

    public List<CourseVo> getByPeriodId(DataAccessAdapter<E> em, long periodId);

    public List<CourseVo> getByProfessorIdAndSubjectId(DataAccessAdapter<E> em, long professorId, long subjectId);

    public List<CourseVo> getCoursesByProfessorNameLikeAndSubjectNameLike(DataAccessAdapter<E> em, String professorName, String subjectName, Long periodId);
}
