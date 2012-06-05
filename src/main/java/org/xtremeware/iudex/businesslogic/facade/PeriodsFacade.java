package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.PeriodVo;

public class PeriodsFacade extends AbstractFacade {

    public PeriodsFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public void deletePeriod(long periodId) 
            throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getPeriodsService().delete(entityManager, periodId);
            transaction.commit();
        } catch (DataBaseException exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
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
    public PeriodVo createPeriod(int year, int semester) 
            throws MultipleMessagesException, DataBaseException, DuplicityException {
       
        PeriodVo periodVo = new PeriodVo();
        periodVo.setYear(year);
        periodVo.setSemester(semester);
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            periodVo = getServiceFactory().getPeriodsService().create(entityManager, periodVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        }finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return periodVo;
    }

    public List<PeriodVo> listPeriods() {
        List<PeriodVo> periodVos;
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            periodVos = getServiceFactory().getPeriodsService().list(entityManager);

        } catch (Exception enException) {
            getServiceFactory().getLogService().error(enException.getMessage(), enException);
            throw new RuntimeException(enException);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return periodVos;

    }
}
