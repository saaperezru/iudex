package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.xtremeware.iudex.helper.Role;
import org.xtremeware.iudex.vo.UserVo;

@javax.persistence.Entity(name = "User")
@NamedQuery(name="getUserByUsernameAndPassword",query="SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password")
@Table(name = "USER_")
public class UserEntity implements Serializable, Entity<UserVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_USER_")
	private Long id;
	@Column(name = "FIRST_NAMES", length = 50, nullable = false)
	private String firstName;
	@Column(name = "LAST_NAMES", length = 50, nullable = false)
	private String lastName;
	@Column(name = "USER_NAME", length = 20, nullable = false)
	private String userName;
	@Column(name = "PASSWORD_", length = 20, nullable = false)
	private String password;
	@Column(name = "ROL", nullable = false)
	private Role rol;
	@Column(name = "ACTIVE")
	private boolean active;
	//ADD ASOCIATION
	@OneToMany(mappedBy = "id")
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
