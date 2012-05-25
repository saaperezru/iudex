package org.xtremeware.iudex.dao.sql.removeimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectsRemoveBehavior implements Remove {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemove<SubjectEntity> simpleRemove;

    public SubjectsRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemove simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void remove(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        removeSubjectRatings(entityManager, entity.getId());
        removeCourses(entityManager, entity.getId());
        getSimpleRemove().remove(entityManager, entity);
    }

    public SimpleRemove<SubjectEntity> getSimpleRemove() {
        return simpleRemove;
    }

    private void removeSubjectRatings(EntityManager entityManager, Long entityId)
            throws DataBaseException {

        List<SubjectRatingEntity> subjectRatings = getDaoBuilder().getSubjectRatingDao().
                getByEvaluatedObjectId(entityManager, entityId);
        for (SubjectRatingEntity subjectRatingEntity : subjectRatings) {
            getDaoBuilder().getSubjectRatingDao().remove(entityManager, subjectRatingEntity.getId());
        }
    }
    
    private void removeCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getBySubjectId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                getCourseDao().remove(entityManager, courseEntity.getId());
        }
    }
}
