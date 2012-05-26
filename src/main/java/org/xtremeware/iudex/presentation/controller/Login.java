package org.xtremeware.iudex.presentation.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
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
    @ManagedProperty(value="#{user}")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String logIn() {
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        FacesContext fc = FacesContext.getCurrentInstance();
        if(user.getRequiresCaptcha() && fc.getViewRoot().getViewId().equals("/index.xhtml")) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("loginMessage", "Has hecho muchos intentos de inicio de sesión fallidos, por ello debemos pedirte algunos datos adicionales");
            return "requiresCaptcha";
        }
        
        try {
            UserVo userVo = usersFacade.logIn(userName, password);
            if (userVo != null) {
                user.setId(userVo.getId());
                user.setFirstName(userVo.getFirstName());
                user.setLastName(userVo.getLastName());
                user.setUserName(userVo.getUserName());
                user.setProgramId(userVo.getProgramsId().get(0));
                user.setRole(userVo.getRole());
                user.setLoggedIn(true);
                user.setFailedLoginAttempts(0);
                return "success";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("loginMessage", "Nombre de usuario o contraseña inválidos");
            }
        } catch (Exception ex) {
            ViewHelper.addExceptionFacesMessage("loginForm", ex);
        }
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        return "failure";
    }
}
