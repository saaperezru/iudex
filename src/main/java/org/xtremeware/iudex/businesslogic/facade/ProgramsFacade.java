package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsFacade extends AbstractFacade {

    public ProgramsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public void removeProgram(long programId) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().createProgramsService().remove(entityManager, programId);
            transaction.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(entityManager, transaction, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    /**
     * Persist a new Program with the specified programName
     *
     * @param entityManager entity manager
     * @param programName
     * @return Returns null if there is a problem while persisting (logs all
     * errors) and throws an exception if data isn't valid.
     */
    public ProgramVo addProgram(String programName, int code)
            throws MultipleMessagesException, Exception {
        ProgramVo createdProgramVoVo = null;
        ProgramVo programVo = new ProgramVo();
        programVo.setName(programName);
        programVo.setCode(code);

        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdProgramVoVo = getServiceFactory().createProgramsService().create(entityManager, programVo);
            transaction.commit();
        } catch (MultipleMessagesException exception) {
            throw exception;
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdProgramVoVo;
    }

    public List<ProgramVo> getProgramsAutocomplete(String programName) throws Exception {
        EntityManager entityManager = null;
        List<ProgramVo> programVos = null;
        if (programName == null) {
            programVos = new ArrayList<ProgramVo>();
        } else {
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                programVos = getServiceFactory().
                        createProgramsService().getByNameLike(entityManager, programName);

            } catch (Exception e) {
                getServiceFactory().createLogService().error(e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                FacadesHelper.closeEntityManager(entityManager);
            }
        }
        return programVos;
    }

    public List<ProgramVo> listPrograms() {
        EntityManager entityManager = null;
        List<ProgramVo> programVos = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            programVos = getServiceFactory().createProgramsService().getAll(entityManager);
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return programVos;
    }
}
