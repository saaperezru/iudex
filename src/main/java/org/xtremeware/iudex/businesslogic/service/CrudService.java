package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class CrudService<E extends ValueObject, F extends Entity<E>> {

    private AbstractDaoBuilder daoFactory;
    private Create<F> create;
    private Read<F> read;
    private Update<F> update;
    private Delete delete;

    public CrudService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {
        this.daoFactory = daoFactory;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    protected AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }

    private Create<F> getCreateInterface() {
        return create;
    }

    private Read<F> getReadInterface() {
        return read;
    }

    private Delete getDeleteInterface() {
        return delete;
    }

    private Update<F> getUpdateInterface() {
        return update;
    }

    public E create(EntityManager entityManager, E valueObject) throws
            DataBaseException,
            DuplicityException,
            MultipleMessagesException,
            DataBaseException,
            MaxCommentsLimitReachedException {

        validateVoForCreation(entityManager, valueObject);

        return getCreateInterface().create(entityManager, voToEntity(entityManager, valueObject)).toVo();


    }

    public E read(EntityManager entityManager, long valueObjectid) throws DataBaseException {
        F result = getReadInterface().getById(entityManager, valueObjectid);
        if (result == null) {
            return null;
        } else {
            return result.toVo();
        }
    }

    public void delete(EntityManager entityManager, long valueObjectid)
            throws DataBaseException {
        getDeleteInterface().delete(entityManager, valueObjectid);
    }

    public E update(EntityManager entityManager, E valueObject)
            throws ExternalServiceConnectionException,
            MultipleMessagesException,
            DataBaseException,
            DuplicityException {
        validateVoForUpdate(entityManager, valueObject);


        F entity = getUpdateInterface().update(entityManager, voToEntity(entityManager, valueObject));
        if (entity != null) {
            return entity.toVo();
        } else {
            return null;
        }

    }

    protected abstract void validateVoForCreation(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    protected abstract void validateVoForUpdate(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    protected abstract F voToEntity(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;
}
