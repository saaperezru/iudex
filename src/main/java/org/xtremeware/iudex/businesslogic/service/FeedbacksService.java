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
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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
    public void validateVoForCreation(EntityManager em, FeedbackVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage(
                    "feedback.null");
            throw multipleMessageException;
        }
        if (vo.getFeedbackTypeId() == null) {
            multipleMessageException.addMessage(
                    "feedback.feedbackTypeId.null");
        } else if (getDaoFactory().getFeedbackTypeDao().getById(em, vo.getFeedbackTypeId()) == null) {
            multipleMessageException.addMessage(
                    "feedback.feedbackTypeId.element.notFound");
        }
        if (vo.getDate() == null) {
            multipleMessageException.addMessage(
                    "feedback.date.null");
        }
        if (vo.getContent() == null) {
            multipleMessageException.addMessage(
                    "feedback.content.null");
        } else {
            vo.setContent(SecurityHelper.sanitizeHTML(vo.getContent()));
            if (vo.getContent().length() > 2000) {
                multipleMessageException.addMessage(
                        "feedback.content.tooLong");
            }
            if (vo.getContent().isEmpty()) {
                multipleMessageException.addMessage(
                        "feedback.content.tooShort");
            }
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, FeedbackVo valueObject) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage(
                    "feedback.id.null");
            throw multipleMessageException;
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
    public FeedbackEntity voToEntity(EntityManager em, FeedbackVo vo)
            throws ExternalServiceConnectionException,
            MultipleMessagesException, DataBaseException {

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(vo.getContent());
        feedbackEntity.setDate(vo.getDate());
        feedbackEntity.setId(vo.getId());

        feedbackEntity.setType(getDaoFactory().getFeedbackTypeDao().getById(em,
                vo.getFeedbackTypeId()));

        return feedbackEntity;
    }

    /**
     * Returns a list of FeedbackVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of FeedbackVo
     */
    public List<FeedbackVo> search(EntityManager em, String query)
            throws ExternalServiceConnectionException, DataBaseException {
        query = SecurityHelper.sanitizeHTML(query);
        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().
                getByContentLike(em, query);
        if (feedbackEntitys.isEmpty()) {
            return null;
        }
        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }

    public List<FeedbackVo> getFeedbacksByFeedbackType(EntityManager em, long feedbackTypeId) throws DataBaseException {
        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().
                getByTypeId(em, feedbackTypeId);
        if (feedbackEntitys.isEmpty()) {
            return null;
        }
        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }

    public List<FeedbackVo> getAllFeedbacks(EntityManager em) throws DataBaseException {
        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao()
                .getAll(em);
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
