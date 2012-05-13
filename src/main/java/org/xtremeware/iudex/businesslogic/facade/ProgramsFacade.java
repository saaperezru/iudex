package org.xtremeware.iudex.businesslogic.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
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
    public ProgramVo addProgram(String name) throws MultipleMessagesException, Exception {
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
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return createdVo;
    }

    public List<ProgramVo> getProgramsAutocomplete(String name) throws Exception {
        EntityManager em = null;
        List<ProgramVo> programs = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            programs = getServiceFactory().createProgramsService().getByNameLike(em, name);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
           FacadesHelper.closeEntityManager(em);
        }
        return programs;
    }

    public List<ProgramVo> listPrograms() {
        EntityManager em = null;
        List<ProgramVo> list = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createProgramsService().getAll(em);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return list;
    }
}
