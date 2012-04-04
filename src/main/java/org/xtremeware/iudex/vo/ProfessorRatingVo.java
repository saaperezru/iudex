
package org.xtremeware.iudex.vo;

public class ProfessorRatingVo extends ValueObject{

	private Long id;
	private Long professor;
	private Long user;
	private int value;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ProfessorRatingVo other = (ProfessorRatingVo) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "ProfessorRatingVo{" + "id=" + id + ", professor=" + professor + ", user=" + user + ", value=" + value + '}';
        }

	



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProfessor() {
		return professor;
	}

	public void setProfessor(Long professor) {
		this.professor = professor;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
