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
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwFull;
import org.xtremeware.iudex.presentation.vovw.VoVwFactory;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;
import org.xtremeware.iudex.vo.SubjectVo;

public class SubjectsFacade extends AbstractFacade {

    public SubjectsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getSubjectsAutocomplete(String name) throws Exception {
        EntityManager entityManager = null;
        Map<Long, String> map = new HashMap<Long, String>();
        try {
            if (name != null) {
                entityManager = getEntityManagerFactory().createEntityManager();
                List<SubjectVo> subjects = getServiceFactory().createSubjectsService().getByNameLike(entityManager, name);
                for (SubjectVo s : subjects) {
                    map.put(s.getId(), s.getName());
                }
            }
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, ExternalServiceConnectionException.class);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return map;
    }

    public SubjectRatingVo getSubjectRatingByUserId(long subjectId, long userId) throws Exception {
        EntityManager entityManager = null;
        SubjectRatingVo subject = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            subject = getServiceFactory().createSubjectRatingsService().getByEvaluatedObjectAndUserId(entityManager, subjectId, userId);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return subject;
    }

    public RatingSummaryVo getSubjectsRatingSummary(long subjectId) throws Exception {

        EntityManager entityManager = null;
        RatingSummaryVo summary = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            if (getServiceFactory().createSubjectsService().getById(entityManager, subjectId) != null) {
                summary = getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectId);
            }
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return summary;
    }

    public SubjectRatingVo rateSubject(long userId, long subjectId, int value) throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        SubjectRatingVo rating = null;
        try {
            SubjectRatingVo vo = new SubjectRatingVo();
            vo.setEvaluatedObjectId(subjectId);
            vo.setUserId(userId);
            vo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            rating = getServiceFactory().createSubjectRatingsService().create(entityManager, vo);
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

    public SubjectVo addSubject(Long id, String name, String description) throws MultipleMessagesException, Exception {
        SubjectVo createdVo = null;
        SubjectVo vo = new SubjectVo();
        vo.setName(name);
        vo.setDescription(description);
        vo.setId(id);
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createSubjectsService().create(entityManager, vo);
            tx.commit();
        } catch (MultipleMessagesException e) {
            throw e;
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

    public void removeSubject(long id) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            getServiceFactory().createSubjectsService().remove(entityManager, id);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public SubjectVo updateSubject(Long id, String name, String description) throws MultipleMessagesException, Exception {

        SubjectVo subject = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            subject = new SubjectVo();
            subject.setId(id);
            subject.setName(name);
            subject.setDescription(description);
            subject = getServiceFactory().createSubjectsService().update(entityManager, subject);
            tx.commit();
        
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        }finally {
            FacadesHelper.closeEntityManager(entityManager);
        }

        return subject;
    }

    public SubjectVoVwFull getSubject(long subjectId) throws Exception {
        EntityManager entityManager = null;
        SubjectVoVwFull voVw = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            voVw = VoVwFactory.getSubjectVoVwFull(getServiceFactory().createSubjectsService().getById(entityManager, subjectId), getServiceFactory().createSubjectRatingsService().getSummary(entityManager, subjectId));

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return voVw;
    }
}
