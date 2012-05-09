package org.xtremeware.iudex.helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josebermeo
 */
public class MultipleMessageException extends Exception{
    
    private List<Exception> exceptions;

    public MultipleMessageException() {
        this.exceptions = new ArrayList<Exception>();
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }
    
}
