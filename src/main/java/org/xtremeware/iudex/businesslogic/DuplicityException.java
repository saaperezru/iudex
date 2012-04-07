package org.xtremeware.iudex.businesslogic;

/**
 *
 * @author josebermeo
 */
public class DuplicityException extends Exception {

    public DuplicityException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public DuplicityException(String string) {
        super(string);
    }

    public DuplicityException() {
    }
}
