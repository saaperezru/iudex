package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.dao.PeriodDaoInterface;
import org.xtremeware.iudex.entity.PeriodEntity;

/**
 * DAO for the period entities. Implements additionally some useful finders by
 * year or by year and semester
 *
 * @author healarconr
 */
public class PeriodDao extends CrudDao<PeriodEntity> implements PeriodDaoInterface {

    /**
     * Returns the list of all periods entities
     *
     * @param em the entity manager
     * @return a list with all the periods
     */
    @Override
    public List<PeriodEntity> getAll(EntityManager em) {
        checkEntityManager(em);
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
    @Override
    public List<PeriodEntity> getByYear(EntityManager em, int year) {
        checkEntityManager(em);
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
    @Override
    public PeriodEntity getByYearAndSemester(EntityManager em, int year, int semester) {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getPeriodByYearAndSemester", PeriodEntity.class).setParameter("year", year).setParameter("semester", semester).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    @Override
    protected Class getEntityClass() {
        return PeriodEntity.class;
    }
}