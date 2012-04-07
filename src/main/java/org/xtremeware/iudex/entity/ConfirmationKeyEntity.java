package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;

@javax.persistence.Entity(name = "ConfirmationKey")
@NamedQuery(name = "getByConfirmationKey",
query = "SELECT result FROM ConfirmationKeyEntity result "
+ "WHERE result.confirmationKey = :confirmationKey")
@Table(name = "CONFIRMATION_KEY")
public class ConfirmationKeyEntity implements Serializable, Entity<ConfirmationKeyVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID_USER_")
	private Long id;
	
        @Temporal(javax.persistence.TemporalType.DATE)
	@Column(name = "EXPIRATION_DATE", nullable = false)
	private Date expirationDate;
	
        @Column(name = "CONFIRMATION_KEY", length = 20, nullable = false)
	private String confirmationKey;
	
        @OneToOne
	@JoinColumn(name="ID_USER_", referencedColumnName="ID_USER_")
	private UserEntity user;

	@Override
	public ConfirmationKeyVo toVo() {
		ConfirmationKeyVo vo = new ConfirmationKeyVo();
		vo.setId(getId());
		vo.setExpirationDate(getExpirationDate());
		vo.setConfirmationKey(getConfirmationKey());
		vo.setUserId(getUser().getId());
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
            final ConfirmationKeyEntity other = (ConfirmationKeyEntity) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            if (this.expirationDate != other.expirationDate && (this.expirationDate == null || !this.expirationDate.equals(other.expirationDate))) {
                return false;
            }
            if ((this.confirmationKey == null) ? (other.confirmationKey != null) : !this.confirmationKey.equals(other.confirmationKey)) {
                return false;
            }
            if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
            hash = 29 * hash + (this.expirationDate != null ? this.expirationDate.hashCode() : 0);
            hash = 29 * hash + (this.confirmationKey != null ? this.confirmationKey.hashCode() : 0);
            hash = 29 * hash + (this.user != null ? this.user.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "ConfirmationKeyEntity{" + "id=" + id + ", expirationDate=" + expirationDate + ", confirmationKey=" + confirmationKey + ", user=" + user + '}';
        }

	public String getConfirmationKey() {
		return confirmationKey;
	}

	public void setConfirmationKey(String confirmationKey) {
		this.confirmationKey = confirmationKey;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
