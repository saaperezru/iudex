package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.FeedbackDao;
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
    public void validateVo(EntityManager em, FeedbackVo vo) throws InvalidVoException {
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
        if (vo.getContent().length() > 2000) {
            throw new InvalidVoException("Invalid content length in the provided FeedbackVo");
        }
    }

    @Override
    public FeedbackEntity voToEntity(EntityManager em, FeedbackVo vo) throws InvalidVoException {
        
        validateVo(em, vo);

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(vo.getContent());
        feedbackEntity.setDate(vo.getDate());
        feedbackEntity.setId(vo.getId());

        feedbackEntity.setType(getDaoFactory().getFeedbackTypeDao().getById(em, vo.getFeedbackTypeId()));

        return feedbackEntity;
    }

    public List<FeedbackVo> search(EntityManager em, String query) {
        List<FeedbackEntity> feedbackEntitys = ((FeedbackDao) this.getDao()).getByContentLike(em, query);
        if (feedbackEntitys.isEmpty()) {
            return null;
        }
        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }
}
