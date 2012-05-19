package org.xtremeware.iudex.dao.internal;

import org.xtremeware.iudex.entity.ProfessorRatingEntity;

/**
 * DAO for the ProfessorRating entities. Implements additionally some useful
 * finders by professor and user
 *
 * @author juan
 */
public class ProfessorRatingDao extends BinaryRatingDao<ProfessorRatingEntity> {

    @Override
    protected Class getEntityClass() {
        return ProfessorRatingEntity.class;
    }

    @Override
    protected String getQueryForEvaluatedObjectId() {
        return "getProfessorRatingByProfessorId";
    }

    @Override
    protected String getQueryForEvaluatedObjectIdAndUserId() {
        return "getProfessorRatingByProfessorIdAndUserId";
    }

    @Override
    protected String getQueryForRatingByUserId() {
        return "getProfessorRatingByUserId";
    }

    @Override
    protected String getQueryForPositiveRating() {
        return "countPositiveProfessorRating";
    }

    @Override
    protected String getQueryForNegativeRating() {
        return "countNegativeProfessorRating";
    }
}
