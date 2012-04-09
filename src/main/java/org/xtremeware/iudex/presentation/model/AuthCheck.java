/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.presentation.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;

/**
 *
 * @author healarconr
 */
@ManagedBean
@SessionScoped
public class AuthCheck {

    private static final String USER_SESSION_KEY = "user";
    
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
    
    public void login(){
        
    }
    
    public void logout(){
        
    }
}
