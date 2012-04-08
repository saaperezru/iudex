package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.ProfessorVo;

@javax.persistence.Entity(name = "Professor")
@NamedQueries({
	@NamedQuery(name="getProfessorByNameLike",query="SELECT p FROM Professor p WHERE p.firstName = :name OR p.lastName = :name")
})
@Table(name = "PROFESSOR")
public class ProfessorEntity implements Serializable, Entity<ProfessorVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PROFESSOR")
	private Long id;
	@Column(name = "FIRST_NAMES", length = 50, nullable = false)
	private String firstName;
	@Column(name = "LAST_NAMES", length = 50, nullable = false)
	private String lastName;
	@Column(name = "URL_IMAGE", length = 255)
	private String website;
	@Column(name = "URL_WEB", length = 255)
	private String imageUrl;
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	@Column(name = "E_MAIL", length = 50)
	private String email;

	@Override
	public ProfessorVo toVo() {
		ProfessorVo vo = new ProfessorVo();
		vo.setId(this.getId());
		vo.setFirstName(this.getFirstName());
		vo.setLastName(this.getLastName());
		vo.setWebsite(this.getWebsite());
		vo.setImageUrl(this.getImageUrl());
		vo.setDescription(this.getDescription());
		vo.setEmail(this.getEmail());
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
		final ProfessorEntity other = (ProfessorEntity) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "ProfessorEntity{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", website=" + website + ", imageUrl=" + imageUrl + ", description=" + description + ", email=" + email + '}';
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
