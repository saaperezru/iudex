/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.presentation.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
    @ManagedProperty(value = "#login")
    private Login login;
    
    public boolean check(String roles) {
        String[] rolesArray = roles.split(",");
        String userRol = "";
        for (String rol : rolesArray) {
            if (userRol.equals(rol)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USER_SESSION_KEY) != null;
    }
    
    public void login() {
         UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
         UserVo userVo = null;
        try {
            userVo = usersFacade.logIn(login.getUserName(), login.getPassword());
            ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).setAttribute(USER_SESSION_KEY, userVo);
        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage("loginform", facesMessage);
        }
    }
    
    public void logout(){
        
    }
}
