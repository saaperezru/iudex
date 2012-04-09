package org.xtremeware.iudex.helper;

public class ExternalServiceConnectionException extends Exception {

    public ExternalServiceConnectionException(String message) {
        super(message);
    }

    public ExternalServiceConnectionException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
}
