package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ProgramVo;

public class ProgramsService extends CrudService<ProgramVo, ProgramEntity> {

    private final int MAX_PROGRAMNAME_LENGTH;

    public ProgramsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<ProgramEntity>(daoFactory.getProgramDao()),
                new SimpleRead<ProgramEntity>(daoFactory.getProgramDao()),
                new SimpleUpdate<ProgramEntity>(daoFactory.getProgramDao()),
                new SimpleRemove<ProgramEntity>(daoFactory.getProgramDao()));
        MAX_PROGRAMNAME_LENGTH = 50;
    }

    @Override
    public void validateVo(EntityManager em, ProgramVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("program.null");
            throw multipleMessageException;
        }
        if (vo.getName() == null) {
            multipleMessageException.addMessage(
                    "program.name.null");
        } else {
            vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
            if (vo.getName().length() > MAX_PROGRAMNAME_LENGTH) {
                multipleMessageException.addMessage(
                        "program.name.tooLong");
            }
            if (vo.getName().isEmpty()) {
                multipleMessageException.addMessage(
                        "program.name.tooShort");
            }
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public ProgramEntity voToEntity(EntityManager em, ProgramVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        validateVo(em, vo);
        ProgramEntity entity = new ProgramEntity();
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        return entity;

    }

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the entity manager
     * @param name
     * @return Return a list of
     * <code>ProgramVo></code> objects that contain the given name
     */
    public List<ProgramVo> getByNameLike(EntityManager em, String name)
            throws ExternalServiceConnectionException, DataBaseException {
        name = SecurityHelper.sanitizeHTML(name);
        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        for (ProgramEntity entity : getDaoFactory().getProgramDao().
                getByNameLike(em, name)) {
            list.add(entity.toVo());
        }
        return list;
    }

    public List<ProgramVo> getAll(EntityManager em)
            throws ExternalServiceConnectionException, DataBaseException {
        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        for (ProgramEntity entity : getDaoFactory().getProgramDao().getAll(em)) {
            list.add(entity.toVo());
        }
        return list;
    }
}
