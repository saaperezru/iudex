package org.xtremeware.iudex.businesslogic.service;

/**
 *
 * @author juan
 */
public class InactiveUserException extends Exception {

    public InactiveUserException(Throwable cause) {
        super(cause);
    }

    public InactiveUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InactiveUserException(String message) {
        super(message);
    }

    public InactiveUserException() {
    }
}
