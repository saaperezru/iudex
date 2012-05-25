package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class FeedbacksFacade extends AbstractFacade {

    public FeedbacksFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public List<FeedbackTypeVo> getFeedbackTypes() throws Exception {
        List<FeedbackTypeVo> feedbackTypeVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackTypeVos = getServiceFactory().createFeedbackTypesService().list(entityManager);

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackTypeVos;
    }

    public List<FeedbackVo> getFeedbacksByFeedbackType(long feedbackTypeId) throws Exception {
        List<FeedbackVo> feedbackVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackVos = getServiceFactory().
                    createFeedbacksService().getFeedbacksByFeedbackType(entityManager, feedbackTypeId);
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVos;
    }

    public List<FeedbackVo> getAllFeedbacks() throws Exception {
        List<FeedbackVo> feedbackVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackVos = getServiceFactory().
                    createFeedbacksService().getAllFeedbacks(entityManager);
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVos;
    }

    public FeedbackVo addFeedback(long feedbackType, String feedbackcontent, Date date) throws MultipleMessagesException, Exception {
        
        FeedbackVo feedbackVo = new FeedbackVo();
        feedbackVo.setContent(feedbackcontent);
        feedbackVo.setDate(date);
        feedbackVo.setFeedbackTypeId(feedbackType);
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            feedbackVo = getServiceFactory().createFeedbacksService().create(entityManager, feedbackVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVo;
    }
}
