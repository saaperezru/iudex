package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
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
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            getServiceFactory().createProgramsService().remove(entityManager, id);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
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
    public ProgramVo addProgram(String name,int code) throws MultipleMessagesException, Exception {
        ProgramVo createdVo = null;
        ProgramVo vo = new ProgramVo();
        vo.setName(name);
        vo.setCode(code);
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createProgramsService().create(entityManager, vo);
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

    public List<ProgramVo> getProgramsAutocomplete(String name) throws Exception {
        EntityManager entityManager = null;
        List<ProgramVo> programs = null;
        if(name == null){
            return new ArrayList<ProgramVo>();
        }
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            programs = getServiceFactory().createProgramsService().getByNameLike(entityManager, name);

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
           FacadesHelper.closeEntityManager(entityManager);
        }
        return programs;
    }

    public List<ProgramVo> listPrograms() {
        EntityManager entityManager = null;
        List<ProgramVo> list = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            list = getServiceFactory().createProgramsService().getAll(entityManager);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return list;
    }
}
