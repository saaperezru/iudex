package org.xtremeware.iudex.vo;

import org.xtremeware.iudex.helper.Role;

public class UserVo extends ValueObject{

	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private Role role;
	private boolean active;
	private ProgramVo program;
	

	@Override
	public boolean equals(Object ob) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int compareTo(ValueObject vo) {
		throw new UnsupportedOperationException("Not supported yet.");
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

	public ProgramVo getProgram() {
		return program;
	}

	public void setProgram(ProgramVo program) {
		this.program = program;
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
