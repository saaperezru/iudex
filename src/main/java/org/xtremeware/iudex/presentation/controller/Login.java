package org.xtremeware.iudex.presentation.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        try {
            UserVo userVo = usersFacade.logIn(userName, password);
            if (userVo != null) {
                HttpSession session = (HttpSession) fc.getExternalContext().
                        getSession(true);
                user.setId(userVo.getId());
                user.setFirstName(userVo.getFirstName());
                user.setLastName(userVo.getLastName());
                user.setUserName(userVo.getUserName());
                user.setProgramId(userVo.getProgramsId().get(0));
                user.setRole(userVo.getRole());
                user.setLoggedIn(true);
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
