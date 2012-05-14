package org.xtremeware.iudex.presentation.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.helper.ViewHelper;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Login {

    private String userName;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String logIn() {
        FacesContext fc = FacesContext.getCurrentInstance();
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        try {
            UserVo userVo = usersFacade.logIn(userName, password);
            if (userVo != null) {
                HttpSession session = (HttpSession) fc.getExternalContext().
                        getSession(true);
                session.setAttribute(User.USER_SESSION_KEY, userVo);
                session.setAttribute(User.ROLE_SESSION_KEY,
                        userVo.getRole());
                return "success";
            } else {
                FacesContext.getCurrentInstance().addMessage("loginForm",
                        new FacesMessage(
                        "Nombre de usuario o contraseña inválidos"));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("loginForm",
                    new FacesMessage(ViewHelper.getExceptionMessage(ex)));
        }
        return "failure";
    }
}
