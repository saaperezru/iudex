package org.xtremeware.iudex.da;

public interface AbstractDataAccessFactory {

    public DataAccessAdapter createDataAccess() throws DataAccessException;
}
