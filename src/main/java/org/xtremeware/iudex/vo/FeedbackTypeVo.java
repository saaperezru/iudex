package org.xtremeware.iudex.vo;

public class FeedbackTypeVo extends ValueObject{

	private Long id;
	private String name;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FeedbackTypeVo other = (FeedbackTypeVo) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "FeedbackTypeVo{" + "id=" + id + ", name=" + name + '}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}