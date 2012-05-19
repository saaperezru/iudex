package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
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
public class ProfessorsRemove implements RemoveInterface {
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
    public void remove(EntityManager em, Long id) throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoFactory().getProfessorRatingDao().getByProfessorId(em, id);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoFactory().getProfessorRatingDao().remove(em, rating.getId());
        }


        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseEntity> courses = getDaoFactory().getCourseDao().getByProfessorId(em, id);

        CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseEntity course : courses) {
            courseService.remove(em, course.getId());
        }

        getDaoFactory().getProfessorDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
    
}
