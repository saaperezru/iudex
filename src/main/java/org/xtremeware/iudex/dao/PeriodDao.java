package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the period entities.
 *
 * @author healarconr
 */
public interface PeriodDao extends CrudDao<PeriodEntity> {

    /**
     * Returns the list of all periods entities
     *
     * @param em the entity manager
     * @return a list with all the periods
     */
    List<PeriodEntity> getAll(EntityManager entityManager)
            throws DataBaseException;

    /**
     * Returns a list of period entities which year is equal to the year
     * argument
     *
     * @param entityManager the entity manager
     * @param year the year
     * @return a list of matched period entities
     */
    List<PeriodEntity> getByYear(EntityManager entityManager, int year)
            throws DataBaseException;

    /**
     * Returns a period entity which year and semester match the given arguments
     *
     * @param entityManager the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    PeriodEntity getByYearAndSemester(EntityManager entityManager, int year,
            int semester) throws DataBaseException;
}
