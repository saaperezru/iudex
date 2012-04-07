/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 * Supports CRUD operations on the periods submited to the system
 * 
 * @author juan
 */
public class PeriodsService extends SimpleCrudService<PeriodVo, PeriodEntity> {

    /**
     * Constructor
     * 
     * @param daoFactory the daoFactory
     */
    public PeriodsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * Returns a list with all the periods
     * 
     * @return all the periods submitted
     */
    public List<PeriodVo> list(EntityManager em) {
        List<PeriodEntity> entities = getDaoFactory().getPeriodDao().getAll(em);

        if (entities.isEmpty()) {
            return null;
        }

        List<PeriodVo> vos = new ArrayList<PeriodVo>();

        for (PeriodEntity e : entities) {
            vos.add(e.toVo());
        }

        return vos;
    }

    /**
     * Returns the PeriodDao from DaoFactory
     * 
     * @return CommentDao
     */
    @Override
    protected Dao<PeriodEntity> getDao() {
        return getDaoFactory().getPeriodDao();
    }

    /**
     * Validates wheter the PeriodVo object satisfies the business rules
     * and contains correct references to other objects
     * 
     * @param em the entity manager
     * @param vo the PeriodVo
     * @throws InvalidVoException in case the business rules are violated
     */
    @Override
    public void validateVo(EntityManager em, PeriodVo vo) throws InvalidVoException {
        if (vo.getSemester() < 1 || vo.getSemester() > 3) {
            throw new InvalidVoException("int Semester in the provided PeriodVo must be greater than 1 and less than 3");
        } else if (vo.getYear() < 0) {
            throw new InvalidVoException("int Year in the provided PeriodVo must be possitive");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Creates a Entity with the data of the value object
     * 
     * @param em the entity manager
     * @param vo the PeriodVo
     * @return an Entity with the Period value object data
     * @throws InvalidVoException 
     */
    @Override
    public PeriodEntity voToEntity(EntityManager em, PeriodVo vo) throws InvalidVoException {

        validateVo(em, vo);

        PeriodEntity entity = new PeriodEntity();

        entity.setId(vo.getId());
        entity.setSemester(vo.getSemester());
        entity.setYear(vo.getYear());

        return entity;

    }
}
