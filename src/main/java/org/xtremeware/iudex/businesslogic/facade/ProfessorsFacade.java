package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.presentation.vovw.VoVwFactory;
import org.xtremeware.iudex.vo.*;

public class ProfessorsFacade extends AbstractFacade {

    public ProfessorsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getProfessorsAutocomplete(String name) throws Exception {
        EntityManager entityManager = null;
        Map<Long, String> map = new HashMap<Long, String>();
        if(name == null){
            return map;
        }
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            List<ProfessorVo> professors = getServiceFactory().createProfessorsService().getByNameLike(entityManager, name);
            for (ProfessorVo p : professors) {
                map.put(p.getId(), p.getFirstName() + " " + p.getLastName());
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return map;
    }

    public ProfessorVo addProfessor(ProfessorVo vo) throws MultipleMessagesException, Exception {
        ProfessorVo createdVo = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createProfessorsService().create(entityManager, vo);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdVo;
    }
    
    public ProfessorVo editProfessor(ProfessorVo vo) throws MultipleMessagesException, Exception {
        ProfessorVo createdVo = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createProfessorsService().update(entityManager, vo);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdVo;
    }

    public void removeProfessor(long professorId) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            getServiceFactory().createProfessorsService().remove(entityManager, professorId);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public ProfessorRatingVo getProfessorRatingByUserId(long professorId, long userId) throws Exception {
        EntityManager entityManager = null;
        ProfessorRatingVo rating = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createProfessorRatingsService().getByEvaluatedObjectAndUserId(entityManager, professorId, userId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }

    public ProfessorRatingVo rateProfessor(long professorId, long userId, int value) throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        ProfessorRatingVo rating = null;
        try {
            ProfessorRatingVo vo = new ProfessorRatingVo();
            vo.setEvaluatedObjectId(professorId);
            vo.setUserId(userId);
            vo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            rating = getServiceFactory().createProfessorRatingsService().create(entityManager, vo);
            tx.commit();

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }

    public ProfessorVoVwFull getProfessor(long professorId) throws Exception {
        EntityManager entityManager = null;
        ProfessorVoVwFull voVw = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            ProfessorVo professorVo = getServiceFactory().createProfessorsService().getById(entityManager, professorId);
            if (professorVo != null) {
                voVw = VoVwFactory.getProfessorVoVwFull(professorVo, getServiceFactory().createProfessorRatingsService().getSummary(entityManager, professorId));
            }
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return voVw;
    }

    public RatingSummaryVo getProfessorRatingSummary(long professorId) throws Exception {
        EntityManager entityManager = null;
        RatingSummaryVo summary = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            summary = getServiceFactory().createProfessorRatingsService().getSummary(entityManager, professorId);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return summary;
    }
}
