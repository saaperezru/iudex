package org.xtremeware.iudex.presentation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.facade.ProgramsFacade;
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.helper.Role;
import org.xtremeware.iudex.vo.ProgramVo;
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
    private List<SelectItem> programs;
    @ManagedProperty(value = "#{authCheck}")
    private AuthCheck authCheck;

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

    public AuthCheck getAuthCheck() {
        return authCheck;
    }

    public void setAuthCheck(AuthCheck authCheck) {
        this.authCheck = authCheck;
    }

    public List<SelectItem> getPrograms() {
        if (programs == null) {
            programs = new ArrayList<SelectItem>();
            ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
            List<ProgramVo> programsList = programsFacade.listPrograms();
            if (programsList != null) {
                for (ProgramVo vo : programsList) {
                    programs.add(new SelectItem(vo.getId(), vo.getName()));
                }
            }
        }
        return programs;
    }

    public void setPrograms(List<SelectItem> programs) {
        this.programs = programs;
    }

    public String signUp() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        if (authCheck.isLoggedIn()) {
            fc.getExternalContext().responseSendError(401, "");
            // TODO: Is return needed?
        }
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
        UserVo user = new UserVo();
        user.setUserName(getUserName());
        user.setPassword(getPassword());
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        List<Long> programsList = new ArrayList<Long>();
        if (programId != null) {
            programsList.add(programId);
        }
        user.setProgramsId(programsList);
        user.setRole(Role.STUDENT);
        try {
            usersFacade.addUser(user);
            return "success";
        } catch (MultipleMessagesException ex) {
            fc.addMessage("signUpForm", new FacesMessage(ex.getMessage()));
	    Config.getInstance().getServiceFactory().createLogService().error(ex.getMessage(),ex);
            return "failure";
	}
    }
}
