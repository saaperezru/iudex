/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.presentation.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author healarconr
 */
@ManagedBean
@SessionScoped
public class AuthCheck {

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
    
    public void login(){
        
    }
    
    public void logout(){
        
    }
}
