package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsService extends CrudService<ProgramVo, ProgramEntity> {

    private static final int MAX_PROGRAM_NAME_LENGTH = 50;

    public ProgramsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {
        super(daoFactory, create, read, update, delete);
    }

    @Override
    public void validateVoForCreation(EntityManager entityManager, ProgramVo programVo)
            throws  MultipleMessagesException {

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();

        if (programVo == null) {
            multipleMessageException.addMessage("program.null");
            throw multipleMessageException;
        }
        if (programVo.getName() == null) {
            multipleMessageException.addMessage(
                    "program.name.null");
        } else {
            programVo.setName(SecurityHelper.sanitizeHTML(programVo.getName()));
            if (programVo.getName().length() > MAX_PROGRAM_NAME_LENGTH) {
                multipleMessageException.addMessage(
                        "program.name.tooLong");
            }
            if (programVo.getName().isEmpty()) {
                multipleMessageException.addMessage(
                        "program.name.tooShort");
            }
        }
        if (programVo.getCode() < 0) {
            multipleMessageException.addMessage(
                    "program.code.negativeValue");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public void validateVoForUpdate(EntityManager entityManager, ProgramVo programVo)
            throws MultipleMessagesException, DataBaseException {

        validateVoForCreation(entityManager, programVo);

        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (programVo.getId() == null) {
            multipleMessageException.addMessage("program.id.null");
            throw multipleMessageException;
        }
    }

    @Override
    public ProgramEntity voToEntity(EntityManager em, ProgramVo vo)
            throws MultipleMessagesException {

        ProgramEntity entity = new ProgramEntity();
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setCode(vo.getCode());
        return entity;

    }

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the entity manager
     * @param programName
     * @return Return a list of
     * <code>ProgramVo></code> objects that contain the given name
     */
    public List<ProgramVo> getByNameLike(EntityManager entityManager, String programName)
            throws DataBaseException {

        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        List<ProgramEntity> programEntitys = getDaoFactory().getProgramDao().
                getByNameLike(entityManager,
                SecurityHelper.sanitizeHTML(programName));
        for (ProgramEntity entity : programEntitys) {
            list.add(entity.toVo());
        }
        return list;
    }

    public List<ProgramVo> getAll(EntityManager em)
            throws DataBaseException {
        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        List<ProgramEntity> programEntitys = getDaoFactory().getProgramDao().getAll(em);
        for (ProgramEntity entity : programEntitys) {
            list.add(entity.toVo());
        }
        return list;
    }
}
