/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic;

/**
 *
 * @author josebermeo
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public InvalidCredentialsException(String string) {
        super(string);
    }

    public InvalidCredentialsException() {
    }
}
