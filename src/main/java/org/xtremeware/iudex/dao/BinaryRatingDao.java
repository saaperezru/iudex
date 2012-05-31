package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public interface BinaryRatingDao<E extends Entity> extends CrudDao<E> {

    List<E> getByEvaluatedObjectId(EntityManager entityManager,
            Long evaluatedObjectId) throws DataBaseException;

    E getByEvaluatedObjectIdAndUserId(EntityManager entityManager,
            Long evaluatedObjectId, Long userId) throws DataBaseException;

    List<E> getByUserId(EntityManager entityManager, Long userId)
            throws DataBaseException;

    RatingSummaryVo getSummary(EntityManager entityManager, Long evaluatedObjectId)
            throws DataBaseException;
}
