package org.xtremeware.iudex.dao.sql.deleteimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CoursesDeleteBehavior implements Delete {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<CourseEntity> simpleDelete;

    public CoursesDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleDelete) {
        this.daoBuilder = daoBuilder;
        this.simpleDelete = simpleDelete;
    }

    @Override
    public void delete(EntityManager entityManager, Entity entity) throws DataBaseException {
        deleteCourseRatings(entityManager, entity.getId());
        deleteComments(entityManager, entity.getId());
        getSimpleDelete().delete(entityManager, entity);
    }

    private SimpleDeleteBehavior<CourseEntity> getSimpleDelete() {
        return simpleDelete;
    }
    
    private void deleteCourseRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CourseRatingEntity> courseRatings = getDaoBuilder().
                getCourseRatingDao().getByCourseId(entityManager, entityId);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoBuilder().getCourseRatingDao().delete(entityManager, rating.getId());
        }
    }

    private void deleteComments(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentEntity> commentEntitys = getDaoBuilder().
                getCommentDao().getByCourseId(entityManager, entityId);
        for (CommentEntity commentEntity : commentEntitys) {
            getDaoBuilder().getCommentDao().delete(entityManager, commentEntity.getId());
        }
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
}
