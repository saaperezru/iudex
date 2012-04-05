
package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

@javax.persistence.Entity
public class FeedbackTypeEntity implements Serializable, Entity<FeedbackTypeVo> {
	private static final long serialVersionUID = 1L;
	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Override
	public FeedbackTypeVo toVo() {
		FeedbackTypeVo vo = new FeedbackTypeVo();
		vo.setId(this.getId());
		vo.setName(this.getName());
		return vo;
	}


	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof FeedbackTypeEntity)) {
			return false;
		}
		FeedbackTypeEntity other = (FeedbackTypeEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}


	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "FeedbackTypeEntity{" + "id=" + id + ", name=" + name + '}';
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