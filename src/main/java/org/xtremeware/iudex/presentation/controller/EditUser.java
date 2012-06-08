package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class EditUser implements Serializable {

    private String firstName;
    private String lastName;
    private String password;
    private String actualPassword;
    private Long programId;
    @ManagedProperty(value = "#{user}")
    private User user;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String actualPassword) {
        this.actualPassword = actualPassword;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void editUser() throws DuplicityException {
        FacesContext fc = FacesContext.getCurrentInstance();
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();

        boolean correctPassword = false;

        try {
            if (usersFacade.logIn(user.getUserName(), actualPassword) != null) {
                correctPassword = true;
            }
        } catch (MultipleMessagesException ex) {
            // Wrong password. correctPassword flag is already false.
        } catch (InactiveUserException ex) {
            // This shouldn't happen
        }

        if (!correctPassword) {
            fc.addMessage("editForm:actualPassword", new FacesMessage(
                    "La contraseña actual no es correcta"));
        } else {
            UserVo userVo = new UserVo();
            userVo.setId(user.getId());
            userVo.setFirstName(firstName);
            userVo.setLastName(lastName);
            userVo.setUserName(user.getUserName());
            if (password.isEmpty()) {
                password = actualPassword;
            }
            userVo.setPassword(password);
            userVo.setProgramsId(Arrays.asList(new Long[]{programId}));
            userVo.setRole(user.getRole());
            userVo.setActive(true);

            try {
                usersFacade.updateUser(userVo);
                fc.addMessage(null, new FacesMessage(
                        "El usuario ha sido actualizado exitosamente"));

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setProgramId(programId);
            } catch (MultipleMessagesException ex) {
                List<String> messages = ex.getMessages();
                for (String message : messages) {
                    fc.addMessage(null, new FacesMessage(message));
                }
            }
        }
    }

    public void preRenderView() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            setFirstName(user.getFirstName());
            setLastName(user.getLastName());
            setProgramId(user.getProgramId());
        }
    }
}
