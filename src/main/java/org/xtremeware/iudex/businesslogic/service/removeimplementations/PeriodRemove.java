package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.dao.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CoursesService;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class PeriodRemove implements Remove {

    private AbstractDaoBuilder daoFactory;

    public PeriodRemove(AbstractDaoBuilder daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the period and all the courses associated to it.
     *
     * @param em entity manager
     * @param id id of the period
     */
    @Override
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {

        //TODO fix implementation
        

        getDaoFactory().getPeriodDao().remove(entityManager, entityId);
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }
}
