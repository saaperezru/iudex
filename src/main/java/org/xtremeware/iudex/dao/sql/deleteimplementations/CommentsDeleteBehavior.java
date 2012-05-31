package org.xtremeware.iudex.dao.sql.deleteimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CommentsDeleteBehavior implements Delete<CommentEntity> {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<CommentEntity> simpleDelete;

    public CommentsDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleDelete) {
        this.daoBuilder = daoBuilder;
        this.simpleDelete = simpleDelete;
    }

    @Override
    public void delete(EntityManager entityManager, CommentEntity entity)
            throws DataBaseException {
        List<CommentRatingEntity> commentRatings = getDaoBuilder().
                getCommentRatingDao().getByEvaluatedObjectId(entityManager, entity.getId());
        
        for (CommentRatingEntity rating : commentRatings) {
            getDaoBuilder().getCommentRatingDao().delete(entityManager, rating.getId());
        }
        
        getSimpleDelete().delete(entityManager, entity);
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }

    private SimpleDeleteBehavior<CommentEntity> getSimpleDelete() {
        return simpleDelete;
    }
}
