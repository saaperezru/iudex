package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 * DAO for the periodVo. Implements additionally some useful finders by
 * year or by year and semester
 *
 * @author josebermeo
 */
public interface PeriodDao<E> extends CrudDao<PeriodVo, E> {

    /**
     * Returns the list of all periods entities
     * 
     * @param em the DataAccessAdapter
     * @return a list with all the periods
     */
    public List<PeriodVo> getAll(DataAccessAdapter<E> em)throws DataAccessException;

    /**
     * Returns a list of periods which year is equal to the year
     * argument
     *
     * @param em the DataAccessAdapter
     * @param year the year
     * @return a list of matched period entities
     */
    public List<PeriodVo> getByYear(DataAccessAdapter<E> em, int year)throws DataAccessException;

    /**
     * Returns a period which year and semester match the given arguments
     *
     * @param em the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    public PeriodVo getByYearAndSemester(DataAccessAdapter<E> em, int year, int semester)throws DataAccessException;
}
