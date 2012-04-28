package org.xtremeware.iudex.da;


public abstract class DataAccessAdapter<E> {

    E dataAccess;

    public DataAccessAdapter(E dataAccess) {
        checkDataAccessClass(dataAccess);
        this.dataAccess = dataAccess;
    }

    public E getDataAccess() {
        return dataAccess;
    }

    protected abstract void checkDataAccessClass(E dataAccess);

    protected void checkDataAccessNotNull() {
        if (dataAccess == null) {
            throw new IllegalStateException("The data access object is null");
        }
    }

    public abstract void beginTransaction() throws DataAccessException;

    public abstract void commit() throws DataAccessException;

    public abstract void rollback() throws DataAccessException;

    public abstract void close() throws DataAccessException;

    public abstract String getType();
}
