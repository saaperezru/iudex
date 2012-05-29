package org.xtremeware.iudex.businesslogic.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.hibernate.exception.ConstraintViolationException;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
public class FacadesHelper {

    private static <E extends Exception> void checkException(EntityManager entityManager,
            EntityTransaction transaction, Exception exception, Class<E> exceptionClass,
            boolean rollback) throws
            E {
        if (exceptionClass.isInstance(exception)) {
            if (rollback) {
                rollbackTransaction(entityManager, transaction);
            }
            throw exceptionClass.cast(exception);
        }
    }

    /**
     * Checks if the exception is an instance of the exception class. If so,
     * rolls back the transaction and then throws the exception.
     *
     * @param entityManager the entity manager
     * @param transaction the transaction
     * @param exception the exception to check
     * @param exceptionClass the exception class
     */
    public static <E extends Exception> void checkExceptionAndRollback(
            EntityManager entityManager,
            EntityTransaction transaction, Exception exception, Class<E> exceptionClass) throws
            E {
        checkException(entityManager, transaction, exception, exceptionClass, true);
    }

    public static void checkDuplicityViolation(
            EntityManager entityManager,
            EntityTransaction transaction, Throwable exception) throws DuplicityException {
        if (exception != null) {
            if (exception instanceof ConstraintViolationException) {
                checkExceptionAndRollback(
                        entityManager,
                        transaction,
                        new DuplicityException("entity.exists", exception.getCause()),
                        DuplicityException.class);
            }else{
                checkDuplicityViolation(entityManager, transaction,  exception.getCause());
            }
        }

    }

    /**
     * Checks if the exception is an instance of the exception class. If so,
     * throws it.
     *
     * @param ex the exception to check
     * @param exceptionClass the exception class
     */
    public static <E extends Exception> void checkException(Exception ex,
            Class<E> exceptionClass) throws
            E {
        checkException(null, null, ex, exceptionClass, false);
    }

    /**
     * Silently rolls back a transaction and logs possible exceptions
     *
     * @param em the entity manager
     * @param tx the transaction
     */
    private static void rollbackTransaction(EntityManager em,
            EntityTransaction tx) {
        rollbackTransaction(em, tx, null);
    }

    /**
     * Rolls back a transaction and throws a RuntimeException to wrap the
     * exception argument
     *
     * @param em the entity manager
     * @param tx the entity transaction
     * @param exception the exception to wrap
     */
    public static void rollbackTransaction(EntityManager em,
            EntityTransaction tx,
            Exception exception) {
        try {
            if (em != null && tx != null) {
                tx.rollback();
            }
        } catch (Exception ex) {
            Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
        }
        if (exception != null) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Silently closes the entity manager and logs possible exceptions
     *
     * @param em
     */
    public static void closeEntityManager(EntityManager em) {
        try {
            if (em != null) {
                em.clear();
                em.close();
            }
        } catch (Exception ex) {
            Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
        }
    }
}
