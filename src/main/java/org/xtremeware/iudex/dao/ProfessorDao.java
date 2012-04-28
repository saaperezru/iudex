package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ProfessorVo;

/**
 *
 * @author josebermeo
 */
public interface ProfessorDao<E> extends CrudDao<ProfessorVo, E> {

    public List<ProfessorVo> getByNameLike(DataAccessAdapter<E> em, String name);

    public List<ProfessorVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId);
}
