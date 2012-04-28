package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.CommentVo;

/**
 *
 * @author josebermeo
 */
public interface CommentDao<E> extends CrudDao<CommentVo, E> {

    public List<CommentVo> getByProfessorId(DataAccessAdapter<E> em, long professorId);

    public List<CommentVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId);

    public List<CommentVo> getByUserId(DataAccessAdapter<E> em, long userId);

    public List<CommentVo> getByCourseId(DataAccessAdapter<E> em, long courseId);

    public int getUserCommentsCounter(DataAccessAdapter<E> em, long userId);
}
