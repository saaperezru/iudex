/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.presentation.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@SessionScoped
public class User {

    public static final String USER_SESSION_KEY = "userVo";
    public static final String ROLE_SESSION_KEY = "role";
    
    public boolean checkAuth(String roles) {
        String[] rolesArray = roles.split(",");
        String userRole = FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().get(ROLE_SESSION_KEY).toString();
        for (String role : rolesArray) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().get(USER_SESSION_KEY) != null;
    }

    public String logOut() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().
                getSession(true)).invalidate();
        return "home";
    }

    public String getUserName() {
        if (isLoggedIn()) {
            UserVo user = (UserVo) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get(USER_SESSION_KEY);
            return user.getUserName();
        }
        return "";
    }
}
