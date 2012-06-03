package org.xtremeware.iudex.businesslogic.helper;

import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;

/**
 *
 * @author josebermeo
 */
public interface FacadesHelper {

    /**
     * Checks if the exception is an instance of the exception class. If so,
     * rolls back the transaction and then throws the exception.
     *
     * @param entityManager the entity manager
     * @param transaction the transaction
     * @param exception the exception to check
     * @param exceptionClass the exception class
     */
    <E extends Exception> void checkExceptionAndRollback(
            EntityManager entityManager,
            EntityTransaction transaction, Exception exception, Class<E> exceptionClass) throws
            E;

    void checkDuplicityViolation(
            EntityManager entityManager,
            EntityTransaction transaction, Throwable exception) throws DuplicityException;

    /**
     * Checks if the exception is an instance of the exception class. If so,
     * throws it.
     *
     * @param ex the exception to check
     * @param exceptionClass the exception class
     */
    <E extends Exception> void checkException(Exception ex,
            Class<E> exceptionClass) throws
            E;

    /**
     * Rolls back a transaction and throws a RuntimeException to wrap the
     * exception argument
     *
     * @param em the entity manager
     * @param tx the entity transaction
     * @param exception the exception to wrap
     */
    void rollbackTransaction(EntityManager em,
            EntityTransaction tx,
            Exception exception);

    /**
     * Silently closes the entity manager and logs possible exceptions
     *
     * @param em
     */
    void closeEntityManager(EntityManager em);
}
