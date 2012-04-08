package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.vo.PeriodVo;

public class PeriodsFacade extends AbstractFacade {

	public PeriodsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
		super(serviceFactory, emFactory);
	}

	public void removePeriod(long id) {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			getServiceFactory().createPeriodsService().remove(em, id);
			tx.commit();
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
	}

	/**
	 * Persist a new Period with the specified year and semester.
	 *
	 * @param em entity manager
	 * @param year
	 * @param semester
	 * @return Returns null if there is a problem while persisting (logs all
	 * errors) and throws an exception if data isn't valid.
	 */
	public PeriodVo addPeriod(int year, int semester) throws InvalidVoException {
		PeriodVo createdVo = null;
		PeriodVo vo = new PeriodVo();
		vo.setYear(year);
		vo.setSemester(semester);
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			createdVo = getServiceFactory().createPeriodsService().create(em, vo);
			tx.commit();
		} catch (InvalidVoException e) {
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

	public List<PeriodVo> listPeriods() {
		List<PeriodVo> list = new ArrayList<PeriodVo>();
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			list = getServiceFactory().createPeriodsService().list(em);

		} catch (Exception e) {
			getServiceFactory().createLogService().error(e.getMessage(), e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return list;

	}
}
