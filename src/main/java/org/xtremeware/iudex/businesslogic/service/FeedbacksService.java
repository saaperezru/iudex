package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbacksService extends CrudService<FeedbackVo, FeedbackEntity> {
    
    private static final int MAX_FEEDBACK_LENGTH = 2000;

    /**
     * FeedbacksService constructor
     *
     * @param daoFactory
     */
    public FeedbacksService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {

        super(daoFactory, create, read, update, delete);

    }

    /**
     * Validate the provided FeedbackVo, if the FeedbackVo is not correct the
     * methods throws an exception
     *
     * @param entityManager EntityManager
     * @param feedbackVo FeedbackVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, FeedbackVo feedbackVo)
            throws MultipleMessagesException,DataBaseException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (feedbackVo == null) {
            multipleMessageException.addMessage(
                    "feedback.null");
            throw multipleMessageException;
        }
        if (feedbackVo.getFeedbackTypeId() == null) {
            multipleMessageException.addMessage(
                    "feedback.feedbackTypeId.null");
        } else if (getDaoFactory().getFeedbackTypeDao().read(entityManager, feedbackVo.getFeedbackTypeId()) == null) {
            multipleMessageException.addMessage(
                    "feedback.feedbackTypeId.element.notFound");
        }
        if (feedbackVo.getDate() == null) {
            multipleMessageException.addMessage(
                    "feedback.date.null");
        }
        if (feedbackVo.getContent() == null) {
            multipleMessageException.addMessage(
                    "feedback.content.null");
        } else {
            feedbackVo.setContent(SecurityHelper.sanitizeHTML(feedbackVo.getContent()));
            if (feedbackVo.getContent().length() > MAX_FEEDBACK_LENGTH) {
                multipleMessageException.addMessage(
                        "feedback.content.tooLong");
            }
            if (feedbackVo.getContent().isEmpty()) {
                multipleMessageException.addMessage(
                        "feedback.content.tooShort");
            }
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public void validateVoForUpdate(EntityManager entityManager, FeedbackVo feedbackVo)
            throws MultipleMessagesException, DataBaseException {

        validateVoForCreation(entityManager, feedbackVo);

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (feedbackVo.getId() == null) {
            multipleMessageException.addMessage(
                    "feedback.id.null");
            throw multipleMessageException;
        }
    }

    /**
     * Returns a FeedbackEntity using the information in the provided
     * FeedbackVo.
     *
     * @param entityManager EntityManager
     * @param feedbackVo FeedbackVo
     * @return FeedbackEntity
     * @throws InvalidVoException
     */
    @Override
    public FeedbackEntity voToEntity(EntityManager entityManager, FeedbackVo feedbackVo)
            throws MultipleMessagesException, DataBaseException {

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(feedbackVo.getContent());
        feedbackEntity.setDate(feedbackVo.getDate());
        feedbackEntity.setId(feedbackVo.getId());

        feedbackEntity.setType(getDaoFactory().getFeedbackTypeDao().read(entityManager,
                feedbackVo.getFeedbackTypeId()));

        return feedbackEntity;
    }

    /**
     * Returns a list of FeedbackVo according with the search query
     *
     * @param entityManager EntityManager
     * @param query String with the search parameter
     * @return A list of FeedbackVo
     */
    public List<FeedbackVo> search(EntityManager entityManager, String query)
            throws DataBaseException {
        query = SecurityHelper.sanitizeHTML(query);

        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().
                getByContentLike(entityManager, query);


        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }

    public List<FeedbackVo> getFeedbacksByFeedbackType(EntityManager entityManager, long feedbackTypeId)
            throws DataBaseException {

        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().
                getByTypeId(entityManager, feedbackTypeId);

        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }

    public List<FeedbackVo> getAllFeedbacks(EntityManager entityManager)
            throws DataBaseException {

        List<FeedbackEntity> feedbackEntitys = getDaoFactory().getFeedbackDao().getAll(entityManager);
        ArrayList<FeedbackVo> arrayList = new ArrayList<FeedbackVo>();
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            arrayList.add(feedbackEntity.toVo());
        }
        return arrayList;
    }
}
