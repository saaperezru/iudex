package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import org.xtremeware.iudex.helper.Role;
import org.xtremeware.iudex.vo.UserVo;

@javax.persistence.Entity
@NamedQuery(name="getByUsernameAndPassword",query="SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password")
public class UserEntity implements Serializable, Entity<UserVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Role rol;
    private boolean active;
    //ADD ASOCIATION
    private List<ProgramEntity> programs;

    @Override
    public UserVo toVo() {
        UserVo vo = new UserVo();
        vo.setId(this.getId());
        vo.setFirstName(this.getFirstName());
        vo.setLastName(this.getLastName());
        vo.setUserName(this.getUserName());
        vo.setPassword(this.getPassword());
        vo.setRol(this.getRol());
        ArrayList<Long> programsId = new ArrayList<Long>();
        for (ProgramEntity program : this.getPrograms()) {
            programsId.add(program.getId());
        }
        vo.setProgramsId(programsId);
        return vo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + ", password=" + password + ", rol=" + rol + ", active=" + active + ", programs=" + programs + '}';
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

    public List<ProgramEntity> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramEntity> programs) {
        this.programs = programs;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
