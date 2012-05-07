package org.xtremeware.iudex.presentation.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class AccountConfirmation {

    @ManagedProperty(value = "#{param['key']}")
    private String key;
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void preRenderView() {
        if (key != null) {
            try {
                if (Config.getInstance().getFacadeFactory().getUsersFacade().activateUser(key) != null) {
                    message = "Confirmación exitosa";
                    return;
                }
            } catch (Exception ex) {
            }
        }
        message = "No se pudo confirmar la clave";
    }
}
