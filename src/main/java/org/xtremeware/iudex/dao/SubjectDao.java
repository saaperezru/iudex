package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public interface SubjectDao<E> extends CrudDao<SubjectVo, E> {

    public List<SubjectVo> getByName(DataAccessAdapter<E> em, String name);

    public List<SubjectVo> getByProfessorId(DataAccessAdapter<E> em, long professorId);
}
