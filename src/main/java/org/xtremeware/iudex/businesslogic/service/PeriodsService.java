package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.*;
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
    public PeriodsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {
        
        super(daoFactory, create, read, update, delete);
    }

    /**
     * Returns a list with all the periods
     *
     * @return all the periods submitted
     */
    public List<PeriodVo> list(EntityManager entityManager) 
            throws DataBaseException {
        List<PeriodEntity> entities = getDaoFactory().getPeriodDao().getAll(entityManager);

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
     * @param entityManager the entity manager
     * @param periodVo the PeriodVo
     * @throws InvalidVoException in case the business rules are violated
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, PeriodVo periodVo)
            throws MultipleMessagesException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (periodVo == null) {
            multipleMessageException.addMessage("period.null");
            throw multipleMessageException;
        }
        if (periodVo.getSemester() < MIN_SEMESTER || periodVo.getSemester() > MAX_SEMESTER) {
            multipleMessageException.addMessage(
                    "period.semester.invalidSemester");
        }
        if (periodVo.getYear() < MIN_YEAR) {
            multipleMessageException.addMessage(
                    "period.year.invalidYear");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public void validateVoForUpdate(EntityManager entityManager, PeriodVo periodVo)
            throws MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException {
        
        validateVoForCreation(entityManager, periodVo);
        
         MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (periodVo.getId() == null) {
            multipleMessageException.addMessage("period.id.null");
            throw multipleMessageException;
        }
    }

    /**
     * Creates a Entity with the data of the value object
     *
     * @param entityManager the entity manager
     * @param periodVo the PeriodVo
     * @return an Entity with the Period value object data
     * @throws InvalidVoException
     */
    @Override
    public PeriodEntity voToEntity(EntityManager entityManager, PeriodVo periodVo)
            throws MultipleMessagesException {

        PeriodEntity entity = new PeriodEntity();

        entity.setId(periodVo.getId());
        entity.setSemester(periodVo.getSemester());
        entity.setYear(periodVo.getYear());

        return entity;

    }
}
