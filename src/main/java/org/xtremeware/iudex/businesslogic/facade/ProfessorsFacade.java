package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.vo.*;

public class ProfessorsFacade extends AbstractFacade {

    public ProfessorsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public Map<Long, String> getProfessorsAutocomplete(String name) throws Exception {
        EntityManager em = null;
        Map<Long, String> map = new HashMap<Long, String>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            List<ProfessorVo> professors = getServiceFactory().createProfessorsService().getByNameLike(em, name);
            for (ProfessorVo p : professors) {
                map.put(p.getId(), p.getFirstName() + " " + p.getLastName());
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

    public ProfessorVo addProfessor(ProfessorVo vo) throws MultipleMessagesException, Exception {
        ProfessorVo createdVo = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.commit();
            createdVo = getServiceFactory().createProfessorsService().create(em, vo);
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

    public void removeProfessor(long professorId) throws Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createProfessorsService().remove(em, professorId);
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

    public ProfessorRatingVo getProfessorRatingByUserId(long professorId, long userId) throws Exception {
        EntityManager em = null;
        ProfessorRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createProfessorRatingsService().getByProfessorIdAndUserId(em, professorId, userId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return rating;
    }

    public ProfessorRatingVo rateProfessor(long professorId, long userId, int value) throws MultipleMessagesException, Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        ProfessorRatingVo rating = null;
        try {
            ProfessorRatingVo vo = new ProfessorRatingVo();
            vo.setEvaluetedObjectId(professorId);
            vo.setUser(userId);
            vo.setValue(value);

            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            rating = getServiceFactory().createProfessorRatingsService().getByProfessorIdAndUserId(em, professorId, userId);
            //If there is no existing record in the database, create it
            if (rating == null) {
                rating = getServiceFactory().createProfessorRatingsService().create(em, vo);
            } else {
                //Otherwise update the existing one
                //But first verify bussines rules
                getServiceFactory().createProfessorRatingsService().validateVo(em, vo);
                rating.setValue(value);
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

    public ProfessorVoVwFull getProfessor(long professorId) throws Exception {
        EntityManager em = null;
        ProfessorVoVwFull voVw = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            ProfessorVo vo = getServiceFactory().createProfessorsService().getById(em, professorId);
            RatingSummaryVo summary = getServiceFactory().createProfessorRatingsService().getSummary(em, professorId);
            voVw = new ProfessorVoVwFull(vo, summary);

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

    public RatingSummaryVo getProfessorRatingSummary(long professorId) throws Exception {
        EntityManager em = null;
        RatingSummaryVo summary = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            summary = getServiceFactory().createProfessorRatingsService().getSummary(em, professorId);

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
}
