package org.xtremeware.iudex.dao.sql.removeimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CommentsRemoveBehavior implements Remove<CommentEntity> {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemove<CommentEntity> simpleRemove;

    public CommentsRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemove simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    @Override
    public void remove(EntityManager entityManager, CommentEntity entity)
            throws DataBaseException {
        List<CommentRatingEntity> commentRatings = getDaoBuilder().
                getCommentRatingDao().getByEvaluatedObjectId(entityManager, entity.getId());
        
        for (CommentRatingEntity rating : commentRatings) {
            getDaoBuilder().getCommentRatingDao().remove(entityManager, rating.getId());
        }
        
        getSimpleRemove().remove(entityManager, entity);
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }

    private SimpleRemove<CommentEntity> getSimpleRemove() {
        return simpleRemove;
    }
}
