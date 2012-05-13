package org.xtremeware.iudex.businesslogic.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
public class FacadesHelper {

    private static <E extends Exception> void checkException(EntityManager em,
            EntityTransaction tx, Exception ex, Class<E> exceptionClass,
            boolean rollback) throws
            E {
        if (exceptionClass.isInstance(ex)) {
            if (rollback) {
                rollbackTransaction(em, tx);
            }
            throw exceptionClass.cast(ex);
        }
    }

    /**
     * Checks if the exception is an instance of the exception class. If so, 
     * rolls back the transaction and then throws the exception.
     * @param em the entity manager
     * @param tx the transaction
     * @param ex the exception to check
     * @param exceptionClass the exception class
     */
    public static <E extends Exception> void checkExceptionAndRollback(
            EntityManager em,
            EntityTransaction tx, Exception ex, Class<E> exceptionClass) throws
            E {
        checkException(em, tx, ex, exceptionClass, true);
    }

    /**
     * Checks if the exception is an instance of the exception class. If so, throws
     * it.
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
            Config.getInstance().getServiceFactory().createLogService().error(ex.
                    getMessage(), ex);
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
            Config.getInstance().getServiceFactory().createLogService().error(ex.
                    getMessage(), ex);
        }
    }
}
