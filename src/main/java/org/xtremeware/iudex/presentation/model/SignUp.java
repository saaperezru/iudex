package org.xtremeware.iudex.presentation.model;

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
import org.xtremeware.iudex.businesslogic.facade.UsersFacade;
import org.xtremeware.iudex.helper.Config;
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
    private SelectItem program;
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

    public SelectItem getProgram() {
        return program;
    }

    public void setProgram(SelectItem program) {
        this.program = program;
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
        if (program != null) {
            programsList.add((Long) program.getValue());
        }
        user.setProgramsId(programsList);
        try {
            usersFacade.addUser(user);
            return "success";
        } catch (InvalidVoException ex) {
            fc.addMessage("signUpForm", new FacesMessage(ex.getLocalizedMessage()));
            return "failure";
        }
    }
}
