package org.xtremeware.iudex.businesslogic.facade;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

public class FeedbacksFacade extends AbstractFacade {

    public FeedbacksFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public List<FeedbackTypeVo> getFeedbackTypes() throws Exception {
        List<FeedbackTypeVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createFeedbackTypesService().list(em);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
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
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return list;
    }
    
    public List<FeedbackVo> getAllFeedbacks() throws Exception {
        List<FeedbackVo> list = null;
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createFeedbacksService().getAllFeedbacks(em);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return list;
    }

    public FeedbackVo addFeedback(long feedbackType, String content, Date date) throws MultipleMessagesException, Exception {
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
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw  e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return createdVo;
    }
}
