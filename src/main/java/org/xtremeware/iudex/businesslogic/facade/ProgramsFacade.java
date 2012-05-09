package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessageException;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsFacade extends AbstractFacade {

    public ProgramsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public void removeProgram(long id) throws Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createProgramsService().remove(em, id);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
    }

    /**
     * Persist a new Program with the specified name
     *
     * @param em entity manager
     * @param name
     * @return Returns null if there is a problem while persisting (logs all
     * errors) and throws an exception if data isn't valid.
     */
    public ProgramVo addProgram(String name) throws MultipleMessageException {
        ProgramVo createdVo = null;
        ProgramVo vo = new ProgramVo();
        vo.setName(name);
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createProgramsService().create(em, vo);
            tx.commit();
        } catch (MultipleMessageException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return createdVo;
    }

    public Map<Long, String> getProgramsAutocomplete(String name) throws Exception {
        EntityManager em = null;
        Map<Long, String> map = new HashMap<Long, String>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            List<ProgramVo> programs = getServiceFactory().createProgramsService().getByNameLike(em, name);
            for (ProgramVo p : programs) {
                map.put(p.getId(), p.getName());
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

    public List<ProgramVo> listPrograms() {
        EntityManager em = null;
        EntityTransaction tx = null;
        List<ProgramVo> list = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            list = getServiceFactory().createProgramsService().getAll(em);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return list;
    }
}
