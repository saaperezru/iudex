package org.xtremeware.iudex.presentation.controller;

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
    private boolean confirmed;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        checkConfirmation();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    private void checkConfirmation() {
        if (key != null && Config.getInstance().getFacadeFactory().getUsersFacade().
                activateUser(key) != null) {
            confirmed = true;
            return;
        }
        confirmed = false;
    }
}
