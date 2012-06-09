package org.xtremeware.iudex.helper;

/**
 *
 * @author josebermeo
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
}
