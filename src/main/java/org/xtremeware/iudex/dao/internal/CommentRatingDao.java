package org.xtremeware.iudex.dao.internal;

import org.xtremeware.iudex.entity.CommentRatingEntity;

/**
 *
 * @author josebermeo
 */
public class CommentRatingDao extends BinaryRatingDao<CommentRatingEntity> {

    @Override
    protected Class getEntityClass() {
        return CommentRatingEntity.class;
    }

    @Override
    protected String getQueryForEvaluatedObjectId() {
        return "getCommentRatingByCommentId";
    }

    @Override
    protected String getQueryForEvaluatedObjectIdAndUserId() {
        return "getCommentRatingByCommentIdAndUserId";
    }

    @Override
    protected String getQueryForRatingByUserId() {
        return "getCommentRatingByUserId";
    }

    @Override
    protected String getQueryForPositiveRating() {
        return "countPositiveCommentRating";
    }

    @Override
    protected String getQueryForNegativeRating() {
        return "countNegativeCommentRating";
    }
}
