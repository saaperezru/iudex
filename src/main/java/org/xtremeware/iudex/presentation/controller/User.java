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

    // TODO: Make this option configurable
    private static final int LOGIN_ATTEMPTS_FOR_CAPTCHA = 3;
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private Long programId;
    private Role role;
    private boolean loggedIn;
    private int failedLoginAttempts = 0;
    private boolean requiresCaptcha = false;
    //TODO : Get default user image from a centralized site
    private static String imageUrl = "/resources/images/user.png";

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

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
        requiresCaptcha = failedLoginAttempts >= LOGIN_ATTEMPTS_FOR_CAPTCHA - 1;
    }

    public boolean getRequiresCaptcha() {
        return requiresCaptcha;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String logOut() {
        firstName = null;
        lastName = null;
        userName = null;
        programId = null;
        role = null;
        loggedIn = false;
        failedLoginAttempts = 0;
        requiresCaptcha = false;
        return "home";
    }
}
