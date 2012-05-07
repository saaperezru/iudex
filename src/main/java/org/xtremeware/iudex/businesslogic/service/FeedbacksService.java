package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbacksService extends CrudService<FeedbackVo, FeedbackEntity> {

    /**
     * FeedbacksService constructor
     *
     * @param daoFactory
     */
    public FeedbacksService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<FeedbackEntity>(daoFactory.getFeedbackDao()),
                new SimpleRead<FeedbackEntity>(daoFactory.getFeedbackDao()),
                new SimpleUpdate<FeedbackEntity>(daoFactory.getFeedbackDao()),
                new SimpleRemove<FeedbackEntity>(daoFactory.getFeedbackDao()));
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
    public void validateVo(EntityManager em, FeedbackVo vo) throws InvalidVoException, ExternalServiceConnectionException {
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
     * Returns a FeedbackEntity using the information in the provided
     * FeedbackVo.
     *
     * @param em EntityManager
     * @param vo FeedbackVo
     * @return FeedbackEntity
     * @throws InvalidVoException
     */
    @Override
    public FeedbackEntity voToEntity(EntityManager em, FeedbackVo vo) throws InvalidVoException,
            ExternalServiceConnectionException {

        validateVo(em, vo);

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(vo.getContent());
        feedbackEntity.setDate(vo.getDate());
        feedbackEntity.setId(vo.getId());

        feedbackEntity.setType(getDaoFactory().getFeedbackTypeDao().getById(em, vo.getFeedbackTypeId()));

        return feedbackEntity;
    }

    /**
     * Returns a list of FeedbackVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of FeedbackVo
     */
    public List<FeedbackVo> search(EntityManager em, String query) throws ExternalServiceConnectionException {
        if (query == null) {
            throw new IllegalArgumentException("Null query for a Feedback comment search");
        }
        query = SecurityHelper.sanitizeHTML(query);
        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().getByContentLike(em, query);
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
