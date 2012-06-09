package org.xtremeware.iudex.presentation.helper;

import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ApplicationScoped
public class ViewHelper {

    public static void addExceptionFacesMessage(String clientId,
            MultipleMessagesException ex) {
        FacesContext fc = FacesContext.getCurrentInstance();
        for (String message : ex.getMessages()) {
            fc.addMessage(clientId, new FacesMessage(
                    getExceptionMessage(message)));
        }
    }

    private static String getExceptionMessage(String message) {
        return Config.getInstance().getFacadeFactory().getExceptionsFacade().
                getMessage(
                message, FacesContext.getCurrentInstance().getViewRoot().
                getLocale());
    }

    public static void addExceptionFacesMessage(String clientId, Exception ex) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(getExceptionMessage(ex.getMessage())));
    }

    public String addErrorClass(String clientId) {
        if (hasMessages(clientId)) {
            return "error";
        } else {
            return "";
        }
    }

    public boolean hasMessages(String clientId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = fc.getMessages(clientId);
        return it.hasNext();
    }

    public boolean hasMessagesRecursive(String clientId) {
        Iterator<String> it = FacesContext.getCurrentInstance().
                getClientIdsWithMessages();
        String id;
        while (it.hasNext()) {
            id = it.next();
            if (id != null && id.startsWith(clientId)) {
                return true;
            }
        }
        return false;
    }

    public String getMessage(String clientId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = fc.getMessages(clientId);
        if (it.hasNext()) {
            return it.next().getSummary();
        } else {
            return "";
        }
    }
}
