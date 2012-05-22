package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.PeriodRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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
    public void validateVoForCreation(EntityManager em, PeriodVo vo)
            throws MultipleMessagesException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("period.null");
            throw multipleMessageException;
        }
        if (vo.getSemester() < MIN_SEMESTER || vo.getSemester() > MAX_SEMESTER) {
            multipleMessageException.addMessage(
                    "period.semester.invalidSemester");
        }
        if (vo.getYear() < MIN_YEAR) {
            multipleMessageException.addMessage(
                    "period.year.invalidYear");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public void validateVoForUpdate(EntityManager entityManager, PeriodVo valueObject)
            throws MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("period.id.null");
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
            throws MultipleMessagesException {

        PeriodEntity entity = new PeriodEntity();

        entity.setId(vo.getId());
        entity.setSemester(vo.getSemester());
        entity.setYear(vo.getYear());

        return entity;

    }
}
