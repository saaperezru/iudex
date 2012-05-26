package org.xtremeware.iudex.presentation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.helper.Role;
import org.xtremeware.iudex.presentation.helper.ViewHelper;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class SignUp {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Long programId;
    @ManagedProperty(value = "#{user}")
    private User user;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
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

    public String signUp() throws IOException, Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        if (user.isLoggedIn()) {
            fc.getExternalContext().responseSendError(401, "");
            return null;
        }
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        UserVo userVo = new UserVo();
        userVo.setUserName(getUserName());
        userVo.setPassword(getPassword());
        userVo.setFirstName(getFirstName());
        userVo.setLastName(getLastName());
        List<Long> programsList = new ArrayList<Long>();
        if (programId != null) {
            programsList.add(programId);
        }
        userVo.setProgramsId(programsList);
        userVo.setRole(Role.STUDENT);
        try {
            usersFacade.addUser(userVo);
            return "success";
        } catch(DuplicityException ex){
            ViewHelper.addExceptionFacesMessage("signUpForm", ex);
            return "failure";
        } catch (MultipleMessagesException ex) {
            ViewHelper.addExceptionFacesMessage(password, ex);
            return "failure";
        }
    }
}
