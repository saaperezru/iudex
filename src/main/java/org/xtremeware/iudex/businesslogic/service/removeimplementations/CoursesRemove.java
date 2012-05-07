package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CommentsService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author josebermeo
 */
public class CoursesRemove implements RemoveInterface {

    private AbstractDaoFactory daoFactory;

    public CoursesRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void remove(EntityManager em, Long id) {
        List<CourseRatingEntity> courseRatings = getDaoFactory().getCourseRatingDao().getByCourseId(em, id);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoFactory().getCourseRatingDao().remove(em, rating.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CommentEntity> comments = getDaoFactory().getCommentDao().getByCourseId(em, id);

        CommentsService commentService = Config.getInstance().getServiceFactory().createCommentsService();
        for (CommentEntity comment : comments) {
            commentService.remove(em, comment.getId());
        }

        getDaoFactory().getCourseDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
