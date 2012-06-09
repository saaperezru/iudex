package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.BinaryRatingDao;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author josebermeo
 */
public abstract class BinaryRatingService<F extends Entity<BinaryRatingVo>> extends CrudService<BinaryRatingVo, F> {

    private BinaryRatingDao<F> dao;

    public BinaryRatingService(AbstractDaoBuilder daoFactory,
            Create create,
            Read reas,
            Update update,
            Delete delete,
            BinaryRatingDao<F> dao) {
        super(daoFactory, create, reas, update, delete);
        this.dao = dao;
    }

    protected BinaryRatingDao<F> getDao() {
        return dao;
    }

    public List<BinaryRatingVo> getByEvaluatedObjectId(EntityManager entityManager,
            long evaluatedObjectId) throws DataBaseException {
        List<BinaryRatingVo> list = new ArrayList<BinaryRatingVo>();
        for (F rating : getDao().
                getByEvaluatedObjectId(entityManager, evaluatedObjectId)) {
            list.add(rating.toVo());
        }
        return list;
    }

    public BinaryRatingVo getByEvaluatedObjectAndUserId(EntityManager entityManager,
            long evaluatedObjectId, long userId) throws DataBaseException {
        F rating = getDao().
                getByEvaluatedObjectIdAndUserId(entityManager, evaluatedObjectId, userId);

        if (rating == null) {
            return null;
        }

        return rating.toVo();
    }

    public RatingSummaryVo getSummary(EntityManager entityManager, long evaluatedObjectId)
            throws DataBaseException {
        return getDao().getSummary(entityManager, evaluatedObjectId);
    }
}
