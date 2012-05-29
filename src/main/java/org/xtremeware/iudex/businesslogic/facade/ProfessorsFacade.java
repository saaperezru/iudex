package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class ProfessorsFacade extends AbstractFacade {

    public ProfessorsFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getProfessorsAutocomplete(String programName) throws Exception {
        EntityManager entityManager = null;
        Map<Long, String> professorsIdAndNames = new HashMap<Long, String>();;
        if (isNotNull(programName)) {
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<ProfessorVo> professorVos = getServiceFactory().getProfessorsService().getByNameLike(entityManager, programName);
                for (ProfessorVo professorVo : professorVos) {
                    professorsIdAndNames.put(
                            professorVo.getId(),
                            professorVo.getFirstName() + " " + professorVo.getLastName());
                }
            } catch (Exception exception) {
                getServiceFactory().getLogService().error(exception.getMessage(), exception);
                throw new RuntimeException(exception);
            } finally {
                FacadesHelper.closeEntityManager(entityManager);
            }
        }
        return professorsIdAndNames;
    }

    public ProfessorVo addProfessor(ProfessorVo professorVo) throws MultipleMessagesException, Exception {
        ProfessorVo createdVo = null;
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdVo = getServiceFactory().getProfessorsService().create(entityManager, professorVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdVo;
    }

    public ProfessorVo editProfessor(ProfessorVo professorVo) throws MultipleMessagesException, Exception {
        ProfessorVo createdProfessorVo = null;
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdProfessorVo = getServiceFactory().getProfessorsService().update(entityManager, professorVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdProfessorVo;
    }

    public void removeProfessor(long professorId) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getProfessorsService().remove(entityManager, professorId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public BinaryRatingVo getProfessorRatingByUserId(long professorId, long userId)
            throws Exception {
        EntityManager entityManager = null;
        BinaryRatingVo binaryRatingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            binaryRatingVo = getServiceFactory().getProfessorRatingsService().getByEvaluatedObjectAndUserId(entityManager, professorId, userId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return binaryRatingVo;
    }

    public BinaryRatingVo rateProfessor(long professorId, long userId, int value)
            throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        BinaryRatingVo binaryRatingVo = null;
        try {
            binaryRatingVo = new BinaryRatingVo();
            binaryRatingVo.setEvaluatedObjectId(professorId);
            binaryRatingVo.setUserId(userId);
            binaryRatingVo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            binaryRatingVo = getServiceFactory().
                    getProfessorRatingsService().create(entityManager, binaryRatingVo);
            transaction.commit();

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return binaryRatingVo;
    }

    public ProfessorVoFull getProfessor(long professorId) throws Exception {
        EntityManager entityManager = null;
        ProfessorVoFull professorVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().getById(entityManager, professorId);
            if (isNotNull(professorVo)) {
                professorVoFull = new ProfessorVoFull(professorVo,
                        getServiceFactory().getProfessorRatingsService().
                        getSummary(entityManager, professorId));
            }
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return professorVoFull;
    }

    public RatingSummaryVo getProfessorRatingSummary(long professorId) throws Exception {
        EntityManager entityManager = null;
        RatingSummaryVo ratingSummaryVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            ratingSummaryVo = getServiceFactory().getProfessorRatingsService().getSummary(entityManager, professorId);

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return ratingSummaryVo;
    }

    private boolean isNotNull(Object object) {
        return object != null;
    }
}
