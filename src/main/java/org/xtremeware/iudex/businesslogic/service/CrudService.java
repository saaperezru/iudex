package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Read;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public abstract class CrudService<E extends ValueObject, F extends Entity<E>> {

    private AbstractDaoBuilder daoFactory;
    private Create<F> createInterface;
    private Read<F> readInterface;
    private Update<F> updateInterface;
    private Remove removeInterface;

    public CrudService(AbstractDaoBuilder daoFactory,
            Create createInterface,
            Read readInterface, Update updateInterface,
            Remove removeInterface) {
        this.daoFactory = daoFactory;
        this.createInterface = createInterface;
        this.readInterface = readInterface;
        this.updateInterface = updateInterface;
        this.removeInterface = removeInterface;
    }

    protected AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }

    private Create<F> getCreateInterface() {
        return createInterface;
    }

    private Read<F> getReadInterface() {
        return readInterface;
    }

    private Remove getRemoveInterface() {
        return removeInterface;
    }

    private Update<F> getUpdateInterface() {
        return updateInterface;
    }

    public E create(EntityManager entityManager, E valueObject) throws MultipleMessagesException,
            ExternalServiceConnectionException,
            DataBaseException,
            DuplicityException {
        validateVoForCreation(entityManager, valueObject);
        try {
            return getCreateInterface().create(entityManager, voToEntity(entityManager, valueObject)).toVo();
        } catch (DataBaseException ex) {
            if (ex.getMessage().equals("entity.exists")) {
                throw new DuplicityException("entity.exists", ex.getCause());
            } else {
                throw ex;
            }
        }

    }

    public E getById(EntityManager entityManager, long valueObjectid) throws DataBaseException {
        F result = getReadInterface().getById(entityManager, valueObjectid);
        if (result == null) {
            return null;
        } else {
            return result.toVo();
        }
    }

    public void remove(EntityManager entityManager, long valueObjectid)
            throws DataBaseException {
        getRemoveInterface().remove(entityManager, valueObjectid);
    }

    public E update(EntityManager entityManager, E valueObject)
            throws ExternalServiceConnectionException,
            MultipleMessagesException,
            DataBaseException,
            DuplicityException {
        validateVoForUpdate(entityManager, valueObject);
        try {
            F entity = getUpdateInterface().update(entityManager, voToEntity(entityManager, valueObject));
            if (entity != null) {
                return entity.toVo();
            } else {
                return null;
            }
        } catch (DataBaseException ex) {
            if (ex.getMessage().equals("entity.exists")) {
                throw new DuplicityException("entity.exists", ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    public abstract void validateVoForCreation(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    public abstract void validateVoForUpdate(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;

    public abstract F voToEntity(EntityManager entityManager, E valueObject)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException;
}
