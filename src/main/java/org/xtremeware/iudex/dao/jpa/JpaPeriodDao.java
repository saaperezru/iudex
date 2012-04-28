package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.PeriodDao;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 * JPADAO for the periodVo. Implements additionally some useful finders by
 * year or by year and semester
 *
 * @author healarconr
 */
public class JpaPeriodDao extends JpaCrudDao<PeriodVo, PeriodEntity> implements PeriodDao<EntityManager>{

    /**
     * Returns the list of all periods entities
     * 
     * @param em the DataAccessAdapter
     * @return a list with all the periods
     */
    @Override
    public List<PeriodVo> getAll(DataAccessAdapter<EntityManager> em) {
        checkDataAccessAdapter(em);
        List<PeriodEntity> list = em.getDataAccess().createNamedQuery("getAllPeriods", PeriodEntity.class).getResultList();
        return entitiesToVos(list);
        
    }

    /**
     * Returns a list of periods which year is equal to the year
     * argument
     *
     * @param em the DataAccessAdapter
     * @param year the year
     * @return a list of matched period entities
     */
    @Override
    public List<PeriodVo> getByYear(DataAccessAdapter<EntityManager> em, int year) {
        checkDataAccessAdapter(em);
        List<PeriodEntity> list = em.getDataAccess().createNamedQuery("getPeriodsByYear", PeriodEntity.class).setParameter("year", year).getResultList();
        return entitiesToVos(list);
    }

    /**
     * Returns a period which year and semester match the given arguments
     *
     * @param em the entity manager
     * @param year the year
     * @param semester the semester
     * @return the matched period entity or null if there is no such entity
     */
    @Override
    public PeriodVo getByYearAndSemester(DataAccessAdapter<EntityManager> em, int year, int semester) {
        checkDataAccessAdapter(em);
        try {
            return em.getDataAccess().createNamedQuery("getPeriodByYearAndSemester", PeriodEntity.class).setParameter("year", year).setParameter("semester", semester).getSingleResult().toVo();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    protected PeriodEntity voToEntity(DataAccessAdapter<EntityManager> em, PeriodVo vo) {
        PeriodEntity entity = new PeriodEntity();

        entity.setId(vo.getId());
        entity.setSemester(vo.getSemester());
        entity.setYear(vo.getYear());

        return entity;
    }

    @Override
    protected Class getEntityClass() {
        return PeriodEntity.class;
    }
    
    private List<PeriodVo> entitiesToVos(List<PeriodEntity> list) {
        ArrayList<PeriodVo> arrayList = new ArrayList<PeriodVo>();
        for (PeriodEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }
}
