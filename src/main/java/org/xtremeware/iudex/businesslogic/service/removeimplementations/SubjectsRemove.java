package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CoursesService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectsRemove implements RemoveInterface {

    private AbstractDaoFactory daoFactory;

    public SubjectsRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the subject and all the subjectRatings and courses associated to
     * it.
     *
     * @param em entity manager
     * @param id id of the subject
     */
    @Override
    public void remove(EntityManager em, Long id) throws DataBaseException {

        List<SubjectRatingEntity> subjectRatings = getDaoFactory().getSubjectRatingDao().
                getByEvaluatedObjectId(em, id);
        for (SubjectRatingEntity rating : subjectRatings) {
            getDaoFactory().getSubjectRatingDao().remove(em, rating.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseEntity> courses = getDaoFactory().getCourseDao().getBySubjectId(em, id);

        CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseEntity course : courses) {
            courseService.remove(em, course.getId());
        }

        getDaoFactory().getSubjectDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
