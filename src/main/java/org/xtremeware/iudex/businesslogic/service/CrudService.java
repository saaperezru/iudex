package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.UpdateInterface;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.ReadInterface;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.CreateInterface;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class CrudService<E extends ValueObject, F extends Entity<E>> {

    private AbstractDaoFactory daoFactory;
    private CreateInterface<F> createInterface;
    private ReadInterface<F> readInterface;
    private UpdateInterface<F> updateInterface;
    private RemoveInterface removeInterface;

    public CrudService(AbstractDaoFactory daoFactory, CreateInterface createInterface,
            ReadInterface readInterface, UpdateInterface updateInterface, RemoveInterface removeInterface) {
        this.daoFactory = daoFactory;
        this.createInterface = createInterface;
        this.readInterface = readInterface;
        this.updateInterface = updateInterface;
        this.removeInterface = removeInterface;
    }

    protected AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }

    private CreateInterface<F> getCreateInterface() {
        return createInterface;
    }

    private ReadInterface<F> getReadInterface() {
        return readInterface;
    }

    private RemoveInterface getRemoveInterface() {
        return removeInterface;
    }

    private UpdateInterface<F> getUpdateInterface() {
        return updateInterface;
    }

    public E create(EntityManager em, E vo) throws InvalidVoException,
            ExternalServiceConnectionException, MaxCommentsLimitReachedException {
        validateVo(em, vo);
        return getCreateInterface().create(em, voToEntity(em, vo)).toVo();

    }

    public E getById(EntityManager em, long id) {
        return getReadInterface().getById(em, id).toVo();
    }

    public void remove(EntityManager em, long id) {
        getRemoveInterface().remove(em, id);
    }

    public E update(EntityManager em, E vo) throws InvalidVoException,
            ExternalServiceConnectionException {
        validateVo(em, vo);
        return getUpdateInterface().update(em, voToEntity(em, vo)).toVo();
    }

    public abstract void validateVo(EntityManager em, E vo) throws InvalidVoException,
            ExternalServiceConnectionException;

    public abstract F voToEntity(EntityManager em, E vo) throws InvalidVoException,
            ExternalServiceConnectionException;
}
