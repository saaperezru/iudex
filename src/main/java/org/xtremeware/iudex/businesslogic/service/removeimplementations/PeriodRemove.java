package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CoursesService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class PeriodRemove implements RemoveInterface {

    private AbstractDaoFactory daoFactory;

    public PeriodRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the period and all the courses associated to it.
     *
     * @param em entity manager
     * @param id id of the period
     */
    @Override
    public void remove(EntityManager em, Long id) throws DataBaseException {

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseEntity> courses = getDaoFactory().getCourseDao().getByPeriodId(em, id);

        for (CourseEntity course : courses) {
            getDaoFactory().getCourseDao().remove(em, course.getId());
        }

        getDaoFactory().getPeriodDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
