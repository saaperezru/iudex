package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ProfessorVo;

@javax.persistence.Entity(name = "Professor")
@NamedQueries({
	@NamedQuery(name = "getProfessorByNameLike",
	query =
	"SELECT p.id FROM Professor p WHERE UPPER(p.firstName) LIKE :name OR UPPER(p.lastName) LIKE :name")
})
@Table(name = "PROFESSOR",
uniqueConstraints = {
	@UniqueConstraint(columnNames = {"FIRST_NAMES", "LAST_NAMES"})})
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
	@Column(name = "URL_WEB", length = 255, unique = true)
	private String website;
	@Column(name = "URL_IMAGE", length = 255, unique = true)
	private String imageUrl;
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	@Column(name = "E_MAIL", length = 50, unique = true)
	private String email;

	@Override
	public ProfessorVo toVo() {
		ProfessorVo vo = new ProfessorVo();
		vo.setId(this.getId());
		vo.setFirstName(SecurityHelper.sanitizeHTML(this.getFirstName()));
		vo.setLastName(SecurityHelper.sanitizeHTML(this.getLastName()));
		if (getWebsite() != null) {
			vo.setWebsite(SecurityHelper.sanitizeHTML(this.getWebsite()));
		} else {
			vo.setWebsite("");
		}
		if (getImageUrl() != null) {
			vo.setImageUrl(SecurityHelper.sanitizeHTML(this.getImageUrl()));
		} else {
			vo.setImageUrl("");
		}
		if (getDescription() != null) {
			vo.setDescription(SecurityHelper.sanitizeHTML(this.getDescription()));
		} else {
			vo.setDescription("");
		}
		if (getEmail() != null) {
			vo.setEmail(SecurityHelper.sanitizeHTML(this.getEmail()));
		} else {
			vo.setEmail("");
		}
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
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
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
		return "ProfessorEntity{" + "id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", website=" + website
				+ ", imageUrl=" + imageUrl + ", description=" + description
				+ ", email=" + email + '}';
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

	@Override
	public Long getId() {
		return id;
	}

	@Override
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
