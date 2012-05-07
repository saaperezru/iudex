/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.presentation.controller;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@SessionScoped
public class AuthCheck {

    private static final String USER_SESSION_KEY = "user";
    private static final String ROLE_SESSION_KEY = "role";

    public boolean check(String roles) {
        String[] rolesArray = roles.split(",");
        String userRole = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ROLE_SESSION_KEY).toString();
        for (String role : rolesArray) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USER_SESSION_KEY) != null;
    }

    public String login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userName = params.get("loginForm:userName");
        String password = params.get("loginForm:password");
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
        try {
            UserVo userVo = usersFacade.logIn(userName, password);
            if (userVo != null) {
                HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
                session.setAttribute(USER_SESSION_KEY, userVo);
                session.setAttribute(ROLE_SESSION_KEY, userVo.getRole());
                return "success";
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("loginForm", new FacesMessage(ex.getMessage()));
        }
        return "failure";
    }

    public void logout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
    }
}
