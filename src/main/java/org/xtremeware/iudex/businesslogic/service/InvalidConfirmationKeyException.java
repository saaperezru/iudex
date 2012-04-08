/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

/**
 *
 * @author juan
 */
public class InvalidConfirmationKeyException extends Exception {


    public InvalidConfirmationKeyException(Throwable cause) {
        super(cause);
    }

    public InvalidConfirmationKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfirmationKeyException(String message) {
        super(message);
    }

    public InvalidConfirmationKeyException() {
    }
    
}
