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
public interface BinaryRatingDaoInterface<E extends Entity>
        extends CrudDaoInterface<E> {

    public List<E> getByEvaluatedObjectId(EntityManager entityManager,
            Long evaluatedObjectId) throws DataBaseException;

    public E getByEvaluatedObjectIdAndUserId(EntityManager entityManager,
            Long evaluatedObjectId, Long userId) throws DataBaseException;

    public List<E> getByUserId(EntityManager entityManager, Long userId) throws
            DataBaseException;

    public RatingSummaryVo getSummary(EntityManager entityManager, Long evaluatedObjectId) 
            throws DataBaseException;
}
