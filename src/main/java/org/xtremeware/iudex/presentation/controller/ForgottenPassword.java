package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class ForgottenPassword implements Serializable {
    
    private String userName;
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void submit() {
        Config.getInstance().getFacadeFactory().getUsersFacade().recoverPassword(
                userName);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                "Si el usuario existe y está activo se ha enviado un mensaje de correo electrónico para recuperar la contraseña"));
    }
}
