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
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVos;
    }

    public List<FeedbackVo> getAllFeedbacks(int firstResult, int maxResults) {
        List<FeedbackVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().getFeedbacksService().getAllFeedbacks(em, firstResult, maxResults);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }

    public FeedbackTypeVo getFeedbackType(long feedbackTypeId) {
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbackTypesService().read(em,
                    feedbackTypeId);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
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
            FacadesHelper.checkException(exception,
                    MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager,
                    transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager,
                    transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return feedbackVo;
    }
    
    public long countAllFeedbacks() {
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbacksService().countAllFeedbacks(em);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }
    
    public long countFeedbacksByFeedbackType(long feedbackTypeId) {
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            return getServiceFactory().getFeedbacksService().countFeedbacksByTypeId(em, feedbackTypeId);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }
}
