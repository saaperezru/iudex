package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.*;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the period entities. Implements additionally some useful finders by
 * year or by year and semester
 *
 * @author healarconr
 */
public class SqlPeriodDao extends SqlCrudDao<PeriodEntity> implements PeriodDao {

    public SqlPeriodDao(Delete delete) {
        super(delete);
    }

    /**
     * Returns the list of all periods entities
     *
     * @param entityManager the entity manager
     * @return a list with all the periods
     */
    @Override
    public List<PeriodEntity> getAll(EntityManager entityManager)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getAllPeriods",
                    PeriodEntity.class).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of period entities which year is equal to the year
     * argument
     *
     * @param entityManager the entity manager
     * @param year the year
     * @return a list of matched period entities
     */
    @Override
    public List<PeriodEntity> getByYear(EntityManager entityManager, int year)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getPeriodsByYear",
                    PeriodEntity.class).setParameter("year", year).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a period entity which year and semester match the given arguments
     *
     * @param entityManager the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    @Override
    public PeriodEntity getByYearAndSemester(EntityManager entityManager, int year,
            int semester) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getPeriodByYearAndSemester",
                    PeriodEntity.class).setParameter("year", year).setParameter("semester", semester).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

    }

    @Override
    protected Class getEntityClass() {
        return PeriodEntity.class;
    }
}
