package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.vo.ValueObject;

/**
 *
 * @author healarconr
 */
public class CrudService<E extends ValueObject> {

    private AbstractDaoFactory daoFactory;

    public CrudService(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
