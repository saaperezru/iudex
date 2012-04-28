package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ProfessorRatingVo;

/**
 *
 * @author josebermeo
 */
public interface ProfessorRatingDao<E> extends CrudDao<ProfessorRatingVo, E> {

    public List<ProfessorRatingVo> getByNameLike(DataAccessAdapter<E> em, String name);
}
