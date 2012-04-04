package org.xtremeware.iudex.vo;

import java.util.List;
import org.xtremeware.iudex.helper.Role;

public class UserVo extends ValueObject{

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Role role;
    private boolean active;
    private List<Long> programsId;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserVo other = (UserVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
            return false;
        }
        if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
            return false;
        }
        if ((this.userName == null) ? (other.userName != null) : !this.userName.equals(other.userName)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (this.programsId != other.programsId && (this.programsId == null || !this.programsId.equals(other.programsId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
        hash = 59 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
        hash = 59 * hash + (this.userName != null ? this.userName.hashCode() : 0);
        hash = 59 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 59 * hash + (this.role != null ? this.role.hashCode() : 0);
        hash = 59 * hash + (this.active ? 1 : 0);
        hash = 59 * hash + (this.programsId != null ? this.programsId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "UserVo{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + ", password=" + password + ", role=" + role + ", active=" + active + ", programsId=" + programsId + '}';
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Long> getProgramsId() {
        return programsId;
    }

    public void setProgramsId(List<Long> programsId) {
        this.programsId = programsId;
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
    
}
