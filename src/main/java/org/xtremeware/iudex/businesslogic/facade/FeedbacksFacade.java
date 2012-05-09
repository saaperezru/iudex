package org.xtremeware.iudex.businesslogic.facade;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessageException;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

public class FeedbacksFacade extends AbstractFacade {

	public FeedbacksFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
		super(serviceFactory, emFactory);
	}

	public List<FeedbackTypeVo> getFeedbackTypes() throws Exception {
		List<FeedbackTypeVo> list = null;
		EntityManager em = null;
		EntityTransaction tx = null;
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

	public FeedbackVo addFeedback(long feedbackType, String content, Date date) throws MultipleMessageException{
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
		} catch (MultipleMessageException e) {
			throw e;
		} catch (Exception e) {
			if (em != null && tx != null) {
				tx.rollback();
			}
			getServiceFactory().createLogService().error(e.getMessage(), e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return createdVo;

	}
}
