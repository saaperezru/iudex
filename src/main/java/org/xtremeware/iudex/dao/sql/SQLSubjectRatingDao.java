package org.xtremeware.iudex.dao.sql;

import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 *
 * @author josebermeo
 */
public class SqlSubjectRatingDao extends SqlBinaryRatingDao<SubjectRatingEntity> {

    @Override
    protected Class getEntityClass() {
        return SubjectRatingEntity.class;
    }

    @Override
    protected String getQueryForEvaluatedObjectId() {
        return "getSubjectRatingBySubjectId";
    }

    @Override
    protected String getQueryForEvaluatedObjectIdAndUserId() {
        return "getSubjectRatingBySubjectIdAndUserId";
    }

    @Override
    protected String getQueryForRatingByUserId() {
        return "getSubjectRatingByIdUser";
    }

    @Override
    protected String getQueryForPositiveRating() {
        return "countPositiveSubjectRating";
    }

    @Override
    protected String getQueryForNegativeRating() {
        return "countNegativeSubjectRating";
    }
}
