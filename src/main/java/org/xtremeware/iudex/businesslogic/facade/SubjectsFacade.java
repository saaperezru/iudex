package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.*;

public class SubjectsFacade extends AbstractFacade {

    public SubjectsFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getSubjectsAutocomplete(String subjectName) {
        EntityManager entityManager = null;
        Map<Long, String> subjectsIdAndName = new HashMap<Long, String>();
        try {
            if (isNotNull(subjectName)) {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<Long> subjectVos = getServiceFactory().
                        getSubjectsService().
                        getByNameLike(entityManager, subjectName);
                for (Long subjectVoId : subjectVos) {
					SubjectVo subjectVo = getServiceFactory().getSubjectsService().read(entityManager, subjectVoId);
                    subjectsIdAndName.put(subjectVo.getId(), subjectVo.getName());
                }
            }
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, ExternalServiceConnectionException.class);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectsIdAndName;
    }

    public BinaryRatingVo getSubjectRatingByUserId(long subjectId, long userId) {
        EntityManager entityManager = null;
        BinaryRatingVo subjectRatingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            subjectRatingVo = getServiceFactory().
                    getSubjectRatingsService().
                    getByEvaluatedObjectAndUserId(entityManager, subjectId, userId);

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectRatingVo;
    }

    public RatingSummaryVo getSubjectsRatingSummary(long subjectId) {

        EntityManager entityManager = null;
        RatingSummaryVo ratingSummaryVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();

            SubjectVo subjectVo = getServiceFactory().
                    getSubjectsService().read(entityManager, subjectId);

            if (isNotNull(subjectVo)) {
                ratingSummaryVo = getServiceFactory().
                        getSubjectRatingsService().getSummary(entityManager, subjectId);
            }

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return ratingSummaryVo;
    }

    public BinaryRatingVo rateSubject(long userId, long subjectId, int value) 
            throws MultipleMessagesException, DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        BinaryRatingVo ratingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
			
			BinaryRatingVo existingRating = getServiceFactory().getSubjectRatingsService().getByEvaluatedObjectAndUserId(entityManager, subjectId, userId);

            transaction = entityManager.getTransaction();
            transaction.begin();

 			if (existingRating == null){
            BinaryRatingVo binaryRatingVo = new BinaryRatingVo();
            binaryRatingVo.setEvaluatedObjectId(subjectId);
            binaryRatingVo.setUserId(userId);
            binaryRatingVo.setValue(value);

            ratingVo = getServiceFactory().
                    getSubjectRatingsService().create(entityManager, binaryRatingVo);
			}else{
				existingRating.setValue(value);
				ratingVo = getServiceFactory().getSubjectRatingsService().update(entityManager, existingRating);
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
        return ratingVo;
    }

    public SubjectVo createSubject(SubjectVo subjectVo) 
            throws MultipleMessagesException, DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        SubjectVo createdSubjectVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdSubjectVo = getServiceFactory().getSubjectsService().create(entityManager, subjectVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdSubjectVo;
    }

    public void deleteSubject(long sbjectId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getSubjectsService().delete(entityManager, sbjectId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public SubjectVo updateSubject(SubjectVo subjectVo) 
            throws MultipleMessagesException, DuplicityException {

        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        SubjectVo createdSubjectVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdSubjectVo = getServiceFactory().getSubjectsService().update(entityManager, subjectVo);
            transaction.commit();

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }

        return createdSubjectVo;
    }

    public SubjectVoFull getSubject(long subjectId) {
        EntityManager entityManager = null;
        SubjectVoFull subjectVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            subjectVoFull = new SubjectVoFull(getServiceFactory().getSubjectsService().read(entityManager, subjectId), getServiceFactory().getSubjectRatingsService().getSummary(entityManager, subjectId));

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectVoFull;
    }

    private boolean isNotNull(Object object) {
        return object != null;
    }
}
