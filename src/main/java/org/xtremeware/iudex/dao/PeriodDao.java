package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 *
 * @author josebermeo
 */
public interface PeriodDao<E> extends CrudDao<PeriodVo, E> {

    public List<PeriodVo> getAll(DataAccessAdapter<E> em);

    public List<PeriodVo> getByYear(DataAccessAdapter<E> em, int year);

    public PeriodVo getByYearAndSemester(DataAccessAdapter<E> em, int year, int semester);
}
