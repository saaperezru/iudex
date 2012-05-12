package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwFull;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;
import org.xtremeware.iudex.vo.SubjectVo;

public class SubjectsFacade extends AbstractFacade {

    public SubjectsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getSubjectsAutocomplete(String name) throws Exception {
        EntityManager em = null;
        Map<Long, String> map = new HashMap<Long, String>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            List<SubjectVo> subjects = getServiceFactory().createSubjectsService().getByNameLike(em, name);
            for (SubjectVo s : subjects) {
                map.put(s.getId(), s.getName());
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return map;
    }

    public SubjectRatingVo getSubjectRatingByUserId(long subjectId, long userId) throws Exception {
        EntityManager em = null;
        SubjectRatingVo subject = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            subject = getServiceFactory().createSubjectRatingsService().getBySubjectIdAndUserId(em, subjectId, userId);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return subject;
    }

    public RatingSummaryVo getSubjectsRatingSummary(long subjectId) throws Exception {

        EntityManager em = null;
        RatingSummaryVo summary = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            if (getServiceFactory().createSubjectsService().getById(em, subjectId) != null) {
                summary = getServiceFactory().createSubjectRatingsService().getSummary(em, subjectId);
            }
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return summary;
    }

    public SubjectRatingVo rateSubject(long userId, long subjectId, int value) throws MultipleMessagesException, Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        SubjectRatingVo rating = null;
        try {
            SubjectRatingVo vo = new SubjectRatingVo();
            vo.setEvaluatedObjectId(subjectId);
            vo.setUser(userId);
            vo.setValue(value);

            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            rating = getServiceFactory().createSubjectRatingsService().getBySubjectIdAndUserId(em, subjectId, userId);
            //If there is no existing record in the database, create it
            if (rating == null) {
                rating = getServiceFactory().createSubjectRatingsService().create(em, vo);
            } else {
                //Otherwise update the existing one
                //But first verify bussines rules
                if (value != rating.getEvaluatedObjectId()) {
                    getServiceFactory().createSubjectRatingsService().validateVo(em, vo);
                    rating.setValue(value);
                    getServiceFactory().createSubjectRatingsService().update(em, rating);
                }
            }
            tx.commit();

        } catch (MultipleMessagesException ex) {
            throw ex;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return rating;
    }

    public SubjectVo addSubject(Long id, String name, String description) throws MultipleMessagesException, Exception {
        SubjectVo createdVo = null;
        SubjectVo vo = new SubjectVo();
        vo.setName(name);
        vo.setDescription(description);
        vo.setId(id);
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createSubjectsService().create(em, vo);
            tx.commit();
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return createdVo;
    }

    public void removeSubject(long id) throws Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createSubjectsService().remove(em, id);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
    }

    public SubjectVo updateSubject(Long id, String name, String description) throws MultipleMessagesException, Exception {

        SubjectVo subject = null;
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            subject = new SubjectVo();
            subject.setId(id);
            subject.setName(name);
            subject.setDescription(description);
            subject = getServiceFactory().createSubjectsService().update(em, subject);
            tx.commit();
        } catch (MultipleMessagesException ex) {
            throw ex;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    getServiceFactory().createLogService().error(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }

        return subject;
    }

    public SubjectVoVwFull getSubject(long subjectId) throws Exception {
        EntityManager em = null;
        SubjectVoVwFull voVw = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            SubjectVo vo = getServiceFactory().createSubjectsService().getById(em, subjectId);
            RatingSummaryVo summary = getServiceFactory().createSubjectRatingsService().getSummary(em, subjectId);
            voVw = new SubjectVoVwFull(vo, summary);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return voVw;
    }
}
