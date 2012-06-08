package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class ProfessorsFacade extends AbstractFacade {

    public ProfessorsFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getProfessorsAutocomplete(String programName) {
        EntityManager entityManager = null;
        Map<Long, String> professorsIdAndNames = new HashMap<Long, String>();
        if (isNotNull(programName)) {
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<Long> professorVos = getServiceFactory().getProfessorsService().getByNameLike(entityManager, programName);
                for (Long professorVoId : professorVos) {
                    ProfessorVo professorVo = getServiceFactory().getProfessorsService().read(entityManager, professorVoId);
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

    public ProfessorVo createProfessor(ProfessorVo professorVo)
            throws MultipleMessagesException, DuplicityException {
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

    public ProfessorVo updateProfessor(ProfessorVo professorVo)
            throws MultipleMessagesException, DuplicityException {
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

    public void deleteProfessor(long professorId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getProfessorsService().delete(entityManager, professorId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public BinaryRatingVo getProfessorRatingByUserId(long professorId, long userId) {
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
            throws MultipleMessagesException, DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        BinaryRatingVo binaryRatingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();

            BinaryRatingVo existingRating = getServiceFactory().getProfessorRatingsService().getByEvaluatedObjectAndUserId(entityManager, professorId, userId);

            transaction = entityManager.getTransaction();
            transaction.begin();

            if (existingRating == null) {
                binaryRatingVo = new BinaryRatingVo();
                binaryRatingVo.setEvaluatedObjectId(professorId);
                binaryRatingVo.setUserId(userId);
                binaryRatingVo.setValue(value);

                binaryRatingVo = getServiceFactory().
                        getProfessorRatingsService().create(entityManager, binaryRatingVo);
            } else {
                existingRating.setValue(value);
                binaryRatingVo = getServiceFactory().getProfessorRatingsService().update(entityManager, existingRating);
            }
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

    public ProfessorVoFull getProfessor(long professorId) {
        EntityManager entityManager = null;
        ProfessorVoFull professorVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            ProfessorVo professorVo = getServiceFactory().getProfessorsService().read(entityManager, professorId);
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

    public RatingSummaryVo getProfessorRatingSummary(long professorId) {
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
