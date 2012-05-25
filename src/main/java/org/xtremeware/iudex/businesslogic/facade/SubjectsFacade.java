package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.*;

public class SubjectsFacade extends AbstractFacade {

    public SubjectsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getSubjectsAutocomplete(String subjectName)
            throws Exception {
        EntityManager entityManager = null;
        Map<Long, String> subjectsIdAndName = new HashMap<Long, String>();
        try {
            if (isNotNull(subjectName)) {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<SubjectVo> subjectVos = getServiceFactory().
                        createSubjectsService().
                        getByNameLike(entityManager, subjectName);
                for (SubjectVo subjectVo : subjectVos) {
                    subjectsIdAndName.put(subjectVo.getId(), subjectVo.getName());
                }
            }
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, ExternalServiceConnectionException.class);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectsIdAndName;
    }

    public BinaryRatingVo getSubjectRatingByUserId(long subjectId, long userId) throws Exception {
        EntityManager entityManager = null;
        BinaryRatingVo subjectRatingVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            subjectRatingVo = getServiceFactory().
                    createSubjectRatingsService().
                    getByEvaluatedObjectAndUserId(entityManager, subjectId, userId);

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectRatingVo;
    }

    public RatingSummaryVo getSubjectsRatingSummary(long subjectId) throws Exception {

        EntityManager entityManager = null;
        RatingSummaryVo ratingSummaryVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();

            SubjectVo subjectVo = getServiceFactory().
                    createSubjectsService().getById(entityManager, subjectId);

            if (isNotNull(subjectVo)) {
                ratingSummaryVo = getServiceFactory().
                        createSubjectRatingsService().getSummary(entityManager, subjectId);
            }

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return ratingSummaryVo;
    }

    public BinaryRatingVo rateSubject(long userId, long subjectId, int value) 
            throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        BinaryRatingVo ratingVo = null;
        try {
            BinaryRatingVo binaryRatingVo = new BinaryRatingVo();
            binaryRatingVo.setEvaluatedObjectId(subjectId);
            binaryRatingVo.setUserId(userId);
            binaryRatingVo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            ratingVo = getServiceFactory().
                    createSubjectRatingsService().create(entityManager, binaryRatingVo);
            
            transaction.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return ratingVo;
    }

    public SubjectVo addSubject(SubjectVo subjectVo) throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            subjectVo = getServiceFactory().createSubjectsService().create(entityManager, subjectVo);
            transaction.commit();
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(
                    entityManager, transaction, exception, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subjectVo;
    }

    public void removeSubject(long sbjectId) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().createSubjectsService().remove(entityManager, sbjectId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public SubjectVo updateSubject(SubjectVo subjectVo) throws MultipleMessagesException, Exception {

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            subjectVo = getServiceFactory().createSubjectsService().update(entityManager, subjectVo);
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

        return subjectVo;
    }

    public SubjectVoFull getSubject(long subjectId) throws Exception {
        EntityManager entityManager = null;
        SubjectVoFull subjectVoFull = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            subjectVoFull = new SubjectVoFull(getServiceFactory().createSubjectsService().getById(entityManager, subjectId), getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectId));

        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
