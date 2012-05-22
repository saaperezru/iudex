package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
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
public class SubjectsRemove implements Remove {

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
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {

        List<SubjectRatingEntity> subjectRatings = getDaoFactory().getSubjectRatingDao().
                getByEvaluatedObjectId(entityManager, entityId);
        for (SubjectRatingEntity subjectRatingEntity : subjectRatings) {
            getDaoFactory().getSubjectRatingDao().remove(entityManager, subjectRatingEntity.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseEntity> courses = getDaoFactory().getCourseDao().getBySubjectId(entityManager, entityId);

        CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseEntity course : courses) {
            courseService.remove(entityManager, course.getId());
        }

        getDaoFactory().getSubjectDao().remove(entityManager, entityId);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
