package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
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
public class PeriodRemove implements Remove {

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
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {

        //TODO fix implementation
        List<CourseEntity> courses = getDaoFactory().
                getCourseDao().getByPeriodId(entityManager, entityId);
        CoursesService coursesService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseEntity course : courses) {
           coursesService.remove(entityManager, course.getId());
        }

        getDaoFactory().getPeriodDao().remove(entityManager, entityId);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
