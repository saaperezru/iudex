
package org.xtremeware.iudex.vo;

public class ProfessorRatingVo extends ValueObject{

	private Long id;
	private ProfessorVo professor;
	private UserVo user;
	private int value;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProfessorVo getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorVo professor) {
		this.professor = professor;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}


}
