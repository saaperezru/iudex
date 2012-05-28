package org.xtremeware.iudex.businesslogic.facade;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class FeedbacksFacade extends AbstractFacade {

    public FeedbacksFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public List<FeedbackTypeVo> getFeedbackTypes() throws Exception {
        List<FeedbackTypeVo> feedbackTypeVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackTypeVos = getServiceFactory().getFeedbackTypesService().list(entityManager);

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackTypeVos;
    }
    
    public List<FeedbackVo> getFeedbacksByFeedbackType(long feedbackTypeId) {
        List<FeedbackVo> feedbackVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackVos = getServiceFactory().
                    getFeedbacksService().getFeedbacksByFeedbackType(entityManager, feedbackTypeId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVos;
    }
    
    public List<FeedbackVo> getAllFeedbacks() {
        List<FeedbackVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().getFeedbacksService().getAllFeedbacks(em);
        } catch (Exception e) {
            getServiceFactory().getLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
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
            feedbackVo = getServiceFactory().getFeedbacksService().create(entityManager, feedbackVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVo;
    }
}
