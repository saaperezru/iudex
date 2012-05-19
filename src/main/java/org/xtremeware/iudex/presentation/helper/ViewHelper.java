package org.xtremeware.iudex.presentation.helper;

import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ApplicationScoped
public class ViewHelper {

    public static String getExceptionMessage(Exception ex) {
        return Config.getInstance().getFacadeFactory().getExceptionsFacade().
                getMessage(
                ex.getMessage(), FacesContext.getCurrentInstance().getViewRoot().
                getLocale());
    }

    public static FacesMessage getExceptionFacesMessage(Exception ex) {
        return new FacesMessage(getExceptionMessage(ex));
    }

    public String addErrorClass(String clientId) {
        if (hasErrors(clientId)) {
            return "error";
        } else {
            return "";
        }
    }

    public boolean hasErrors(String clientId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = fc.getMessages(clientId);
        return it.hasNext();
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
