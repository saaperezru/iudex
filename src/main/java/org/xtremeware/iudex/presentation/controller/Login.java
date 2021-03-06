package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
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
public class Login implements Serializable {

    private static final int MAX_USERNAME_LENGTH = 20;
    private String userName;
    private String password;
    @ManagedProperty(value = "#{user}")
    private User user;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeDomainPart(ComponentSystemEvent event) {
        UIInput input = (UIInput) event.getComponent();
        String submittedValue = input.getSubmittedValue().toString();
        final String domainPart = "@unal.edu.co";
        if (submittedValue.endsWith(domainPart)) {
            submittedValue = submittedValue.substring(0,
                    submittedValue.lastIndexOf(domainPart));
            input.setSubmittedValue(submittedValue);
        }
    }

    public String logIn() {
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        FacesContext fc = FacesContext.getCurrentInstance();
        if (user.getRequiresCaptcha() && fc.getViewRoot().getViewId().equals(
                "/index.xhtml")) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().
                    put("loginMessage",
                    "Has realizado muchos intentos fallidos de inicio de sesión, por ello debemos pedirte algo de información adicional.");
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
                FacesContext.getCurrentInstance().addMessage(
                        "loginForm:userName", new FacesMessage(
                        "Nombre de usuario o contraseña inválidos"));
                FacesContext.getCurrentInstance().addMessage(
                        "loginForm:password", new FacesMessage(
                        "Nombre de usuario o contraseña inválidos"));
            }
        } catch (Exception ex) {
            ViewHelper.addExceptionFacesMessage("loginForm:userName", ex);
            ViewHelper.addExceptionFacesMessage("loginForm:password", ex);
        }
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        return "failure";
    }
}
