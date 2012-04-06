package org.xtremeware.iudex.businesslogic;

/**
 *
 * @author healarconr
 */
public class InvalidVoException extends Exception {

    public InvalidVoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public InvalidVoException(String string) {
        super(string);
    }

    public InvalidVoException() {
    }
}
