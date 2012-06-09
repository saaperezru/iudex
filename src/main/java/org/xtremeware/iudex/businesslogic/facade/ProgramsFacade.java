package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsFacade extends AbstractFacade {

    public ProgramsFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public void deleteProgram(long programId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getProgramsService().delete(entityManager, programId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransactionAndCloseEntityManager(entityManager, transaction, exception);
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
    public ProgramVo createProgram(String programName, int code)
            throws MultipleMessagesException, DuplicityException {
        ProgramVo programVo = new ProgramVo();
        programVo.setName(programName);
        programVo.setCode(code);

        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            programVo = getServiceFactory().getProgramsService().create(entityManager, programVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(entityManager, exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransactionAndCloseEntityManager(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return programVo;
    }

    public List<ProgramVo> getProgramsAutocomplete(String programName){
        EntityManager entityManager = null;
        List<ProgramVo> programVos = null;
        if (programName == null) {
            programVos = new ArrayList<ProgramVo>();
        } else {
            try {
                entityManager = getEntityManagerFactory().createEntityManager();
                programVos = getServiceFactory().
                        getProgramsService().getByNameLike(entityManager, programName);

            } catch (Exception e) {
                getServiceFactory().getLogService().error(e.getMessage(), e);
                FacadesHelper.closeEntityManager(entityManager);
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
            programVos = getServiceFactory().getProgramsService().getAll(entityManager);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return programVos;
    }

	public ProgramVo getProgram(long programId){
        EntityManager entityManager = null;
        ProgramVo program = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            program = getServiceFactory().getProgramsService().read(entityManager,programId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.closeEntityManager(entityManager);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return program;
	}
}
