package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class ProfessorsFacade extends AbstractFacade {

    public ProfessorsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getProfessorsAutocomplete(String programName) throws Exception {
        EntityManager entityManager = null;
        Map<Long, String> professorsIdAndNames = null;
        if (isNotNull(programName)) {
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<ProfessorVo> professorVos = getServiceFactory().createProfessorsService().getByNameLike(entityManager, programName);
                for (ProfessorVo professorVo : professorVos) {
                    professorsIdAndNames.put(
                            professorVo.getId(),
                            professorVo.getFirstName() + " " + professorVo.getLastName());
                }
            } catch (Exception exception) {
                getServiceFactory().createLogService().error(exception.getMessage(), exception);
                throw new RuntimeException(exception);
            } finally {
                FacadesHelper.closeEntityManager(entityManager);
            }
        } else {
            professorsIdAndNames = new HashMap<Long, String>();
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
            createdVo = getServiceFactory().createProfessorsService().create(entityManager, professorVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(
                    entityManager, transaction, exception, DuplicityException.class);
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
            createdProfessorVo = getServiceFactory().createProfessorsService().update(entityManager, professorVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DuplicityException.class);
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
            getServiceFactory().createProfessorsService().remove(entityManager, professorId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
            binaryRatingVo = getServiceFactory().createProfessorRatingsService().getByEvaluatedObjectAndUserId(entityManager, professorId, userId);
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
                    createProfessorRatingsService().create(entityManager, binaryRatingVo);
            transaction.commit();

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(
                    entityManager, transaction, exception, DuplicityException.class);
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
            ProfessorVo professorVo = getServiceFactory().createProfessorsService().getById(entityManager, professorId);
            if (isNotNull(professorVo)) {
                professorVoFull = new ProfessorVoFull(professorVo,
                        getServiceFactory().createProfessorRatingsService().
                        getSummary(entityManager, professorId));
            }
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
            ratingSummaryVo = getServiceFactory().createProfessorRatingsService().getSummary(entityManager, professorId);

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
