package org.xtremeware.iudex.helper;

/**
 *
 * @author josebermeo
 */
public class DataBaseException extends Exception {

    public DataBaseException(Throwable thrwbl) {
        super(thrwbl);
    }

    public DataBaseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public DataBaseException(String string) {
        super(string);
    }

    public DataBaseException() {
    }
}
