package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.helper.ViewHelper;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ViewScoped
public class RecoverPassword implements Serializable {

    private String key;
    private boolean validKey = false;
    private UserVo user;
    private String password;
    private boolean passwordChanged = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        user = Config.getInstance().getFacadeFactory().getUsersFacade().
                validateForgottenPasswordKey(key);
        validKey = user != null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidKey() {
        return validKey;
    }

    public void setValidKey(boolean validKey) {
        this.validKey = validKey;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void submit() {
        if (validKey) {
            try {
                Config.getInstance().getFacadeFactory().getUsersFacade().
                        resetPassword(key, password);
                passwordChanged = true;
            } catch (MultipleMessagesException ex) {
                ViewHelper.addExceptionFacesMessage(key, ex);
            }
        }
    }
}
