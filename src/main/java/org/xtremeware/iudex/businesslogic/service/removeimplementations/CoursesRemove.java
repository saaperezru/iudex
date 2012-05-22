package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CommentsService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CoursesRemove implements Remove {

    private AbstractDaoFactory daoFactory;

    public CoursesRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseRatingEntity> courseRatings = getDaoFactory().
                getCourseRatingDao().getByCourseId(entityManager, entityId);

        for (CourseRatingEntity courseRatingEntity : courseRatings) {
            getDaoFactory().getCourseRatingDao().
                    remove(entityManager, courseRatingEntity.getId());
        }

        //TODO fix implementation
        List<CommentEntity> comments = getDaoFactory().getCommentDao().
                getByCourseId(entityManager, entityId);

        CommentsService commentService = Config.getInstance().
                getServiceFactory().createCommentsService();
        for (CommentEntity comment : comments) {
            commentService.remove(entityManager, comment.getId());
        }

        getDaoFactory().getCourseDao().remove(entityManager, entityId);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
