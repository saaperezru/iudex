/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

/**
 *
 * @author juan
 */
public class InactiveUserException extends Exception{

    public InactiveUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

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
