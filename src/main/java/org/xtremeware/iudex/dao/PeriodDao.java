package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.PeriodEntity;

/**
 * DAO for the period entities. Implements additionally some useful finders by
 * year or by year and semester
 *
 * @author healarconr
 */
public class PeriodDao extends Dao<PeriodEntity> {

    /**
     * Returns the list of all periods entities
     * 
     * @param em the entity manager
     * @return a list with all the periods
     */
    public List<PeriodEntity> getAll(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getAllPeriods", PeriodEntity.class).getResultList();
    }

    /**
     * Returns a list of period entities which year is equal to the year
     * argument
     *
     * @param em the entity manager
     * @param year the year
     * @return a list of matched period entities
     */
    public List<PeriodEntity> getByYear(EntityManager em, int year) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getPeriodsByYear", PeriodEntity.class).setParameter("year", year).getResultList();
    }

    /**
     * Returns a period entity which year and semester match the given arguments
     *
     * @param em the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    public PeriodEntity getByYearAndSemester(EntityManager em, int year, int semester) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        try {
            return em.createNamedQuery("getPeriodByYearAndSemester", PeriodEntity.class).setParameter("year", year).setParameter("semester", semester).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }
}
