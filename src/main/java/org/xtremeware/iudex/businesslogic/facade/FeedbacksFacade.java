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
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

public class FeedbacksFacade extends AbstractFacade {

    public FeedbacksFacade(ServiceBuilder serviceFactory,
            EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public List<FeedbackTypeVo> getFeedbackTypes() {
        List<FeedbackTypeVo> feedbackTypeVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackTypeVos = getServiceFactory().getFeedbackTypesService().list(
                    entityManager);

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(),
                    exception);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackTypeVos;
    }

    public List<FeedbackVo> getFeedbacksByFeedbackType(long feedbackTypeId, int firstResult, int maxResults) {
        List<FeedbackVo> feedbackVos = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            feedbackVos = getServiceFactory().
                    getFeedbacksService().getFeedbacksByFeedbackType(
                    entityManager, feedbackTypeId, firstResult, maxResults);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(),
                    exception);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVos;
    }

    public List<FeedbackVo> getAllFeedbacks(int firstResult, int maxResults) {
        List<FeedbackVo> list = null;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().getFeedbacksService().getAllFeedbacks(entityManager, firstResult, maxResults);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return list;
    }

    public FeedbackTypeVo getFeedbackType(long feedbackTypeId) {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbackTypesService().read(entityManager,
                    feedbackTypeId);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public FeedbackVo createFeedback(long feedbackType, String feedbackcontent,
            Date date)
            throws MultipleMessagesException, DuplicityException {

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
            feedbackVo = getServiceFactory().getFeedbacksService().create(
                    entityManager, feedbackVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(),
                    exception);
            FacadesHelper.checkException(entityManager, exception,
                    MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager,
                    transaction, exception);
            FacadesHelper.rollbackTransactionAndCloseEntityManager(entityManager,
                    transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVo;
    }
    
    public long countAllFeedbacks() {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbacksService().countAllFeedbacks(entityManager);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }
    
    public long countFeedbacksByFeedbackType(long feedbackTypeId) {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbacksService().countFeedbacksByTypeId(entityManager, feedbackTypeId);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }
}
