package org.xtremeware.iudex.dao.sql.deleteimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectsDeleteBehavior implements Delete {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<SubjectEntity> simpleDelete;

    public SubjectsDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleDelete) {
        this.daoBuilder = daoBuilder;
        this.simpleDelete = simpleDelete;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void delete(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        deleteSubjectRatings(entityManager, entity.getId());
        deleteCourses(entityManager, entity.getId());
        getSimpleRemove().delete(entityManager, entity);
    }

    public SimpleDeleteBehavior<SubjectEntity> getSimpleRemove() {
        return simpleDelete;
    }

    private void deleteSubjectRatings(EntityManager entityManager, Long entityId)
            throws DataBaseException {

        List<SubjectRatingEntity> subjectRatings = getDaoBuilder().getSubjectRatingDao().
                getByEvaluatedObjectId(entityManager, entityId);
        for (SubjectRatingEntity subjectRatingEntity : subjectRatings) {
            getDaoBuilder().getSubjectRatingDao().delete(entityManager, subjectRatingEntity.getId());
        }
    }
    
    private void deleteCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getBySubjectId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                getCourseDao().delete(entityManager, courseEntity.getId());
        }
    }
}
