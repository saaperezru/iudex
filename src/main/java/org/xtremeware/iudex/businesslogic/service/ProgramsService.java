package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SimpleRemove;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
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
    public void validateVo(EntityManager em, ProgramVo vo) throws InvalidVoException, ExternalServiceConnectionException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null ProgramVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("String message in the provided ProgramVo cannot be null");
        }
        vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
        if (vo.getName().length() > MAX_PROGRAMNAME_LENGTH) {
            throw new InvalidVoException("Program name too large");
        }
    }

    @Override
    public ProgramEntity voToEntity(EntityManager em, ProgramVo vo) throws InvalidVoException, ExternalServiceConnectionException {
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
    public List<ProgramVo> getByNameLike(EntityManager em, String name) throws ExternalServiceConnectionException {
        name = SecurityHelper.sanitizeHTML(name);
        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        for (ProgramEntity entity : getDaoFactory().getProgramDao().getByNameLike(em, name)) {
            list.add(entity.toVo());
        }
        return list;
    }

    public List<ProgramVo> getAll(EntityManager em) throws ExternalServiceConnectionException {
        ArrayList<ProgramVo> list = new ArrayList<ProgramVo>();
        for (ProgramEntity entity : getDaoFactory().getProgramDao().getAll(em)) {
            list.add(entity.toVo());
        }
        return list;
    }
}
