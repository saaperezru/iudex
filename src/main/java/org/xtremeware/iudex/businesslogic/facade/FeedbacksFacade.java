package org.xtremeware.iudex.businesslogic.facade;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

public class FeedbacksFacade extends AbstractFacade {

    public FeedbacksFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public List<FeedbackTypeVo> getFeedbackTypes() {
        List<FeedbackTypeVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createFeedbackTypesService().list(em);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }
    
    public List<FeedbackVo> getFeedbacksByFeedbackType(long feedbackTypeId) throws Exception {
        List<FeedbackVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createFeedbacksService().getFeedbacksByFeedbackType(em,feedbackTypeId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }
    
    public List<FeedbackVo> getAllFeedbacks() {
        List<FeedbackVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createFeedbacksService().getAllFeedbacks(em);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }

    public FeedbackVo addFeedback(long feedbackType, String content, Date date) throws MultipleMessagesException {
        FeedbackVo createdVo = null;
        FeedbackVo vo = new FeedbackVo();
        vo.setContent(content);
        vo.setDate(date);
        vo.setFeedbackTypeId(feedbackType);
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createFeedbacksService().create(em, vo);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return createdVo;
    }
}
