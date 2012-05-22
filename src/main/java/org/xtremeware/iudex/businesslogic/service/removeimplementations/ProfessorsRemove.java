package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CoursesService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorsRemove implements Remove {

    private AbstractDaoFactory daoFactory;

    public ProfessorsRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the professor and all the professorRatings and courses associated
     * to him.
     *
     * @param em entity manager
     * @param id id of the professor
     */
    @Override
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoFactory().getProfessorRatingDao().
                getByEvaluatedObjectId(entityManager, entityId);
        for (ProfessorRatingEntity professorRatingEntity : professorRatings) {
            getDaoFactory().getProfessorRatingDao().remove(entityManager, professorRatingEntity.getId());
        }

        /**
         * TODO
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseEntity> courses = getDaoFactory().getCourseDao().getByProfessorId(entityManager, entityId);

        CoursesService coursesService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseEntity course : courses) {
           coursesService.remove(entityManager, course.getId());
        }

        getDaoFactory().getProfessorDao().remove(entityManager, entityId);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
