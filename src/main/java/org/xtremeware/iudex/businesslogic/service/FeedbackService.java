package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbackService extends SimpleCrudService<FeedbackVo, FeedbackEntity> {

    public FeedbackService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    protected Dao<FeedbackEntity> getDao() {
        return getDaoFactory().getFeedbackDao();
    }

    @Override
    public void validateVo(FeedbackVo vo) throws InvalidVoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FeedbackEntity voToEntity(FeedbackVo vo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<FeedbackVo> search(EntityManager em, String query) {
        return null;
    }
}
