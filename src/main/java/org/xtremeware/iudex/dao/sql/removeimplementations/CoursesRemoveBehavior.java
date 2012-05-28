package org.xtremeware.iudex.dao.sql.removeimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CoursesRemoveBehavior implements Remove {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemoveBehavior<CourseEntity> simpleRemove;

    public CoursesRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemoveBehavior simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    @Override
    public void remove(EntityManager entityManager, Entity entity) throws DataBaseException {
        removeCourseRatings(entityManager, entity.getId());
        removeComments(entityManager, entity.getId());
        getSimpleRemove().remove(entityManager, entity);
    }

    private SimpleRemoveBehavior<CourseEntity> getSimpleRemove() {
        return simpleRemove;
    }
    
    private void removeCourseRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CourseRatingEntity> courseRatings = getDaoBuilder().
                getCourseRatingDao().getByCourseId(entityManager, entityId);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoBuilder().getCourseRatingDao().remove(entityManager, rating.getId());
        }
    }

    private void removeComments(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentEntity> commentEntitys = getDaoBuilder().
                getCommentDao().getByCourseId(entityManager, entityId);
        for (CommentEntity commentEntity : commentEntitys) {
            getDaoBuilder().getCommentDao().remove(entityManager, commentEntity.getId());
        }
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
}
