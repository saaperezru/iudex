package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.PeriodEntity;

/**
 * DAO Interface for the period entities.
 *
 * @author healarconr
 */
public interface PeriodDaoInterface extends CrudDaoInterface<PeriodEntity> {

    /**
     * Returns the list of all periods entities
     *
     * @param em the entity manager
     * @return a list with all the periods
     */
    public List<PeriodEntity> getAll(EntityManager em);

    /**
     * Returns a list of period entities which year is equal to the year
     * argument
     *
     * @param em the entity manager
     * @param year the year
     * @return a list of matched period entities
     */
    public List<PeriodEntity> getByYear(EntityManager em, int year);

    /**
     * Returns a period entity which year and semester match the given arguments
     *
     * @param em the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    public PeriodEntity getByYearAndSemester(EntityManager em, int year, int semester);
}
