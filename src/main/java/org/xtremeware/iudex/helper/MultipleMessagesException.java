package org.xtremeware.iudex.helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josebermeo
 */
public class MultipleMessagesException extends Exception {

    private List<String> messages;

    public MultipleMessagesException() {
        this.messages = new ArrayList<String>();
    }

    @Override
    public String getMessage() {
        return messages.toString();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
