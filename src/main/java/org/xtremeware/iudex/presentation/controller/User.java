package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.xtremeware.iudex.helper.Role;

/**
 *
 * @author healarconr
 */
@ManagedBean
@SessionScoped
public class User implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private Long programId;
    private Role role;
    private boolean loggedIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean checkPermissions(String roles) {
        String[] rolesArray = roles.split(",");
        for (String userRole : rolesArray) {
            if (userRole.equals(role.toString())) {
                return true;
            }
        }
        return false;
    }

    public String logOut() {
        firstName = null;
        lastName = null;
        userName = null;
        programId = null;
        role = null;
        loggedIn = false;
        return "home";
    }
}
