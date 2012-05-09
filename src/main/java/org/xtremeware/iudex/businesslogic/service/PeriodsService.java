package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.removeimplementations.PeriodRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessageException;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 * Supports CRUD operations on the periods submitted to the system
 *
 * @author juan
 */
public class PeriodsService extends CrudService<PeriodVo, PeriodEntity> {
    
    private final int MIN_YEAR = 1000;
    private final int MIN_SEMESTER = 1;
    private final int MAX_SEMESTER = 3;

    /**
     * Constructor
     *
     * @param daoFactory the daoFactory
     */
    public PeriodsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<PeriodEntity>(daoFactory.getPeriodDao()),
                new SimpleRead<PeriodEntity>(daoFactory.getPeriodDao()),
                new SimpleUpdate<PeriodEntity>(daoFactory.getPeriodDao()),
                new PeriodRemove(daoFactory));
    }

    /**
     * Returns a list with all the periods
     *
     * @return all the periods submitted
     */
    public List<PeriodVo> list(EntityManager em) throws DataBaseException {
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
     * Validates whether the PeriodVo object satisfies the business rules and
     * contains correct references to other objects
     *
     * @param em the entity manager
     * @param vo the PeriodVo
     * @throws InvalidVoException in case the business rules are violated
     */
    @Override
    public void validateVo(EntityManager em, PeriodVo vo) 
            throws MultipleMessageException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        MultipleMessageException multipleMessageException = new MultipleMessageException();
        if (vo == null) {
            multipleMessageException.getExceptions().add(new InvalidVoException("Null PeriodVo"));
            throw multipleMessageException;
        }
        if (vo.getSemester() < MIN_SEMESTER || vo.getSemester() > MAX_SEMESTER) {
            multipleMessageException.getExceptions().add(new InvalidVoException(
                    "Int Semester in the provided PeriodVo must be greater than 1 and less than 3"));
        }
        if (vo.getYear() < MIN_YEAR) {
            multipleMessageException.getExceptions().add(new InvalidVoException(
                    "Int Year in the provided PeriodVo must be possitive"));
        }
        if (!multipleMessageException.getExceptions().isEmpty()) {
            throw multipleMessageException;
        }
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
    public PeriodEntity voToEntity(EntityManager em, PeriodVo vo) 
            throws MultipleMessageException {

        validateVo(em, vo);

        PeriodEntity entity = new PeriodEntity();

        entity.setId(vo.getId());
        entity.setSemester(vo.getSemester());
        entity.setYear(vo.getYear());

        return entity;

    }
}
