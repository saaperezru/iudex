package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.FeedbackDao;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbacksService extends SimpleCrudService<FeedbackVo, FeedbackEntity> {

    /**
     * FeedbacksService constructor
     *
     * @param daoFactory
     */
    public FeedbacksService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaFeedbackDao to be used.
     *
     * @return JpaFeedbackDao
     */
    @Override
    protected CrudDao<FeedbackVo,FeedbackEntity> getDao() {
        return getDaoFactory().getFeedbackDao();
    }

    /**
     * Validate the provided FeedbackVo, if the FeedbackVo is not correct the
     * methods throws an exception
     *
     * @param em EntityManager
     * @param vo FeedbackVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(DataAccessAdapter em, FeedbackVo vo) throws InvalidVoException, DataAccessException, ExternalServiceConnectionException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
	}
        if (vo == null) {
            throw new InvalidVoException("Null FeedbackVo");
        }
        if (vo.getFeedbackTypeId() == null) {
            throw new InvalidVoException("Null feedbackTypeId in the provided FeedbackVo");
        }
        if (getDaoFactory().getFeedbackTypeDao().getById(em, vo.getFeedbackTypeId()) == null) {
            throw new InvalidVoException("No such FeedbackType associated whit FeedbackVo.FeedbackTypeId");
        }
        if (vo.getDate() == null) {
            throw new InvalidVoException("Null date in the provided FeedbackVo");
        }
        if (vo.getContent() == null) {
            throw new InvalidVoException("Invalid content in the the provided FeedbackVo");
        }
        vo.setContent(SecurityHelper.sanitizeHTML(vo.getContent()));
        if (vo.getContent().length() > 2000) {
            throw new InvalidVoException("Invalid content length in the provided FeedbackVo");
        }
    }

    /**
     * Returns a list of FeedbackVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of FeedbackVo
     */
    public List<FeedbackVo> search(DataAccessAdapter em, String query) throws DataAccessException {
        if (query == null) {
            throw new IllegalArgumentException("Null query for a Feedback comment search");
        }
        return ((FeedbackDao) this.getDao()).getByContentLike(em, query);
    }
}
